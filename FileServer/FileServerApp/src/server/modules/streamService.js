var fs = require('fs');
var logger = require('./customLogger.js');

var streamService = {
  video : function(req, res){
    streamContent(req, res, true);
  },
  audio : function(req, res){
    streamContent(req, res, false);
  }
};

function streamContent(req, res, isVideo){

  let file = req.query.filePath;
  let fileContentType = req.query.fileContentType;

  if(isVideo){
    logger.info("received video request:" + file);
  }else{
    logger.info("received audio request:" + file);
  }

	fs.stat(file, function(err, stats) {

		if(err)
		{
			if(err.code === 'ENOENT')
			{
				return res.sendStatus(404);
			}
      logger.error(err);
			return;
		}

		let range = req.headers.range;

		if(!range)
		{
			let err = new Error('Wrong range');
				err.status = 416;
      logger.error(err);
			return;
		}

		let positions = range.replace(/bytes=/, '').split('-');
		let start = parseInt(positions[0], 10);
		let file_size = stats.size;
		let end = positions[1] ? parseInt(positions[1], 10) : file_size - 1;
		let chunksize = (end - start) + 1;
		let head = {
			'Content-Range': 'bytes ' + start + '-' + end + '/' + file_size,
			'Accept-Ranges': 'bytes',
			'Content-Length': chunksize,
			'Content-Type': fileContentType
		}

		res.writeHead(206, head);

		let stream_position = {
			start: start,
			end: end
		}

		let stream = fs.createReadStream(file, stream_position)

		stream.on('open', function() {
			stream.pipe(res);
		})

		stream.on('error', function(err) {
      logger.info("some error occured during stream");
		});

	});

}

module.exports = streamService;
