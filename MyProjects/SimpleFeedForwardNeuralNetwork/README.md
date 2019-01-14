A Simple Feed Forward Neural Network

                Inspired by "Make Your Own Neural Network By Tariq Rashid"

                https://www.amazon.in/Make-Your-Own-Neural-Network-ebook/dp/B01EER4Z4G

HOW TO USE:

                public static void main(String[] args) {
                  try {
                    
                    //Create your input data
                    float[][] inputs = {
                        {0,0,0},
                        {0,0,1},
                        {0,1,0},
                        {0,1,1},
                        {1,0,0},
                        {1,0,1},
                        {1,1,0},
                        {1,1,1}
                    };
                    
                    //Create your target data
                    float[][] targets = {
                        {0,0,0,0,0,0,0},
                        {0,0,0,0,0,0,1},
                        {0,0,0,0,0,1,0},
                        {0,0,0,0,1,0,0},
                        {0,0,0,1,0,0,0},
                        {0,0,1,0,0,0,0},
                        {0,1,0,0,0,0,0},
                        {1,0,0,0,0,0,0}
                    };
                    
                    //Set your network configuration
                    NNConfig config = new NNConfig();
                    config.setInputData(inputs);
                    config.setTargetData(targets);
                    config.setEpochs(5000);
                    config.setLearningRate(0.05f);
                    config.setCheckTrainingRate(1000);
                    
                    //Specifies the layout of the network
                    //here the network is of 4 layers
                    //1 input, 1 output and 2 hidden layers each with 5 nodes
                    config.setNetworkShape(new int[] {inputs[0].length, 5, 5,targets[0].length});
                    
                    //Add your configuration to the NeuralNetwork object
                    NeuralNetwork nn = new NeuralNetwork(config);
                    
                    //Train your network
                    nn.trainSimpleFeedForwardNN();
                    
                    //Save your network to a file
                    NeuralNetworkUtils utils = new NeuralNetworkUtils();
                    utils.writeNetworkToFile(nn, "network.ser");
                    NeuralNetwork newNetwork = utils.readNetworkFromFile("network.ser");
                    if(newNetwork != null) {
                      System.out.println(Arrays.toString(newNetwork.predict(inputs[3])));
                    }else {
                      System.out.println("could not get NN File");
                    }
                  } catch (Exception e) {
                    e.printStackTrace();
                  }

                }
