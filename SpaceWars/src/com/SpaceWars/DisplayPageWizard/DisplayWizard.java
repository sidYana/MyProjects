package com.SpaceWars.DisplayPageWizard;

import com.SpaceWars.AssetLoader.GameAssetLoader;
import com.SpaceWars.DisplayPageWizard.Pages.CustomizePlayerPage;
import com.SpaceWars.DisplayPageWizard.Pages.GamePage;
import com.SpaceWars.DisplayPageWizard.Pages.LobbyPage;
import com.SpaceWars.DisplayPageWizard.Pages.MainMenuPage;
import com.SpaceWars.DisplayPageWizard.Pages.Menu;
import com.SpaceWars.Main.SpaceWarsMain;
import com.SpaceWars.SocketComm.SocketCommunicationUtil;
import com.SpaceWars.Util.GUIAlignmentUtil;

import processing.core.PApplet;

public class DisplayWizard {

	private PApplet context;
	private GameAssetLoader gameAssets;
	private GUIAlignmentUtil alignments;

	private Menu currentMenuPage;
	private MainMenuPage mainMenuPage;
	private CustomizePlayerPage customizePlayerPage;
	private GamePage gamePage;
	private LobbyPage lobbyPage;

	private static MenuTypes currentMenuType;

	public DisplayWizard(PApplet applet) {

		this.context = applet;
		this.alignments = new GUIAlignmentUtil(applet.width, applet.height);
		this.gameAssets = new GameAssetLoader(applet);

		this.alignments.generateAlignments(applet);

		mainMenuPage = new MainMenuPage(applet, alignments);
		customizePlayerPage = new CustomizePlayerPage(applet, alignments, gameAssets);
		lobbyPage = new LobbyPage(applet, alignments, gameAssets);
		gamePage = new GamePage(applet, alignments, gameAssets);

		DisplayWizard.currentMenuType = MenuTypes.MAIN_MENU;
		setCurrentMenuPage(mainMenuPage);
	}

	public void doDraw() {
		try {
			if (SpaceWarsMain.isDebugEnabled()) {
				alignments.showAlignmentPoints(context);
			}
			currentMenuPage.drawPage();
			checkForMenuChange();
		} catch (Exception e) {
			SocketCommunicationUtil.disconnectClient();
			SocketCommunicationUtil.stopDiscoveryServer();
			SocketCommunicationUtil.stopServer();
			DisplayWizard.setCurrentMenuType(MenuTypes.MAIN_MENU);
			mainMenuPage.showErrorPanel("Some error occured", e);
		}
	}

	private void checkForMenuChange() {
		switch (currentMenuType) {
			case MAIN_MENU:
				if (!(currentMenuPage instanceof MainMenuPage)) {
					setCurrentMenuPage(mainMenuPage);
				}
				break;
			case CUSTOMIZE_PLAYER:
				if (!(currentMenuPage instanceof CustomizePlayerPage)) {
					setCurrentMenuPage(customizePlayerPage);
				}
				break;
			case LOBBY:
				if (!(currentMenuPage instanceof LobbyPage)) {
					setCurrentMenuPage(lobbyPage);
				}
				break;
			case START_GAME:
				if (!(currentMenuPage instanceof GamePage)) {
					setCurrentMenuPage(gamePage);
				}
				break;
			default:
				break;
		}
	}

	private void setCurrentMenuPage(Menu menu) {
		if (currentMenuPage != null) {
			currentMenuPage.hide();
		}
		currentMenuPage = menu;
		currentMenuPage.show();
	}

	public static MenuTypes getCurrentMenuType() {
		return currentMenuType;
	}

	public static void setCurrentMenuType(MenuTypes currentMenuType) {
		DisplayWizard.currentMenuType = currentMenuType;
	}

}
