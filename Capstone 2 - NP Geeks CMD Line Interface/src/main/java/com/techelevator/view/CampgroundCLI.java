package com.techelevator.view;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Stack;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import com.techelevator.controller.ParkManager;
import com.techelevator.model.Park;

public class CampgroundCLI {
	private final ParkManager manager;
	private final UserIO      userIO;

	public static void main(String[] args) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("");

		CampgroundCLI application = new CampgroundCLI(dataSource, System.in, System.out);
		application.run();
	}

	public CampgroundCLI(DataSource datasource, InputStream userInput, OutputStream userOutput) {
		userIO  = new UserIO(userInput, userOutput);
		manager = new ParkManager(datasource, userIO);
	}
	
	// Text for menu options
	private static final String MENU_TEXT_all = "**All Parks**";
	// uses park name as flag   MENU_TEXT_park
	private static final String MENU_TEXT_park_campground = "View Campgrounds";
	private static final String MENU_TEXT_search_campground = "Search for Campsites by Campground";
	private static final String MENU_TEXT_search_park = "Search for Campsites in this Park";
	private static final String MENU_TEXT_search_with_filters = "Search for Campsites by Campground with Specific Attributes";
	private static final String MENU_TEXT_show_reservations = "Show Reservations in Park";
	private static final String MENU_TEXT_search_for_reservations = "Search for Reservation";
	private static final String MENU_TEXT_return = "Return to Previous Screen";
	private static final String MENU_TEXT_quit = "Quit";

	public void run() {
		Park     currentPark = null;

		// menu text and menu options
		String[] currentMenu    = {};
		String   currentExit    = "";
		String   header         = "";

		// user selected menu choice and choice history 
		String        choiceCurrent = MENU_TEXT_all;
		Stack<String> choiceHistory = new Stack<String>();

		// message loop
		boolean isRunning = true;
		while( isRunning ) {

			// dialog message handler 
			switch( choiceCurrent ) {
			case MENU_TEXT_all:
				// setup menu
				currentMenu = manager.ParkNames();
				currentExit = MENU_TEXT_quit;
				header      = "Select a Park for further details";
				break;

			case MENU_TEXT_search_for_reservations:
				userIO.println(currentPark + " National Park - Reservation Search\n");
				userIO.println(Report.listCampgrounds(currentPark, currentPark.campgrounds()));

				// setup menu
				currentMenu = new String[] {
						MENU_TEXT_search_park,
						MENU_TEXT_search_campground,
						MENU_TEXT_search_with_filters
				};
				currentExit = MENU_TEXT_return;
				header = "Select a Command";
				break;
				
			case MENU_TEXT_park_campground:
				userIO.println(currentPark + " National Park - Campgrounds\n");
				userIO.println(Report.listCampgrounds(currentPark, currentPark.campgrounds()));

				// setup menu
				currentMenu = new String[] {
						MENU_TEXT_search_park,
						MENU_TEXT_search_campground,
						MENU_TEXT_search_with_filters
				};
				currentExit = MENU_TEXT_return;
				header = "Select a Command";
				break;

			case MENU_TEXT_search_campground:
				manager.dialogSearchByCampground( currentPark );
				choiceCurrent = MENU_TEXT_return;
				break;

			case MENU_TEXT_search_park:
				manager.dialogSearchByPark( currentPark );
				choiceCurrent = MENU_TEXT_return;
				break;

			case MENU_TEXT_show_reservations:
				manager.getParkReservations( currentPark );
				choiceCurrent = MENU_TEXT_return;
				break;
				
			case MENU_TEXT_search_with_filters:
				manager.dialogSearchByCampgroundWithConditions( currentPark );
				choiceCurrent = MENU_TEXT_return;
				break;
				
			default: // aka MENU_TEXT_park
				currentPark = manager.getParkByName(choiceCurrent);
				userIO.println( Report.parkInformation(currentPark) );
				currentMenu = new String[] {
						MENU_TEXT_park_campground,
						MENU_TEXT_search_for_reservations,
						MENU_TEXT_show_reservations
				};
				currentExit = MENU_TEXT_return;
				header = "Select a Command";
			}

			// menu dialog
			String oldChoice = choiceCurrent;
			if( ! choiceCurrent.equals(MENU_TEXT_return) ) {
				choiceCurrent = userIO.getChoiceFromOptions(currentMenu, currentExit, header);
			}
			
			// exit message handler
			switch( choiceCurrent ) {
			case MENU_TEXT_return:
				choiceCurrent = choiceHistory.pop();
				break;

			case MENU_TEXT_quit:
				isRunning = false;
				break;

			default:
				choiceHistory.push(oldChoice);
			}
		} // while( isRunning )
	} // run()
}
