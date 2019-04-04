package com.techelevator.view;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.techelevator.model.Campground;
import com.techelevator.model.Campsite;
import com.techelevator.model.Park;
import com.techelevator.model.Reservation;

import java.text.DateFormatSymbols;

public class Report {
	static final NumberFormat moneyFormat = NumberFormat.getCurrencyInstance();
	static final NumberFormat integerFormat = NumberFormat.getInstance();
	static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("LL/dd/yyyy");
	static final DateFormatSymbols dateSymbol = new DateFormatSymbols();


	private static String monthName(int month) {
		return dateSymbol.getMonths()[month-1];
	}

	/**
	 * Create campsite information report with campground name included.
	 * 
	 * @param park {@code Park} object containing the report data.
	 * @return Printable "Park Information" report
	 */
	public static String listReservationsInPark(List<Reservation> customers) {
		StringBuffer result = new StringBuffer();
		
		if (customers.size() != 0) {
			// create format command for report
			final String hdrFormat = "%-20s | %-15s | %-15s\n";
			final String linFormat = "%-20s  %-15s  %-15s\n";
			final String heading = String.format(hdrFormat, "Name", "Arrival Date", "Departure Date");
			final String hline = UserIO.repeat("-", heading.length()) + "\n";
	
			result.append("\nAll park reservations in the coming month.\n");
			result.append(hline);
			result.append(heading);
			result.append(hline);
			for( Reservation customer: customers ) {
				result.append(String.format(linFormat,
						customer.getName().replace("Reservation", ""),
						customer.getFromDate().toString(),
						customer.getToDate().toString()
						));
			}
			result.append(hline);
		} else {
			result.append("\nNo reservations have been made for this park for the next month.\n");
		}
		return result.toString();
	}
	/**
	 * Create campsite information report with campground name included.
	 * 
	 * @param park {@code Park} object containing the report data.
	 * @return Printable "Park Information" report
	 */
	//TODO refactor to take park
	public static String listCampsitesInPark(List<Campground> camps, ArrayList<Campsite>[] sitesForCamp) {
		// find the length of the longest camp ground name
		int maxLengthOf = camps.stream()
				.mapToInt(i -> i.getName().length())
				.max().orElse(0);

		// create format command for report
		final String hdrFormat = "     %-" + maxLengthOf + "s | %-8s | %-10s | %-10s | %-13s | %-7s | %8s\n";
		final String linFormat = " #%1d  %-" + maxLengthOf + "s   %-8d   %-10d   %-10s   %-13s   %-7s   %8s\n";
		final String heading = String.format(hdrFormat, "Campground", "Site No.", "Max Occup.", "Accessible", "Max RV Length", "Utility", "Cost");
		final String hline = UserIO.repeat("-", heading.length()) + "\n";

		StringBuffer result = new StringBuffer();
		result.append("\nResults Matching Your Search Criteria\n");
		result.append(hline);
		result.append(heading);
		result.append(hline);

		// for each camp ground
		for( int n = 0; n < camps.size(); n++ ) {
			final List<Campsite> sites = sitesForCamp[n];
			
			// define cost of stay in the campground
			final String strFee = moneyFormat.format(sites.isEmpty()? 0: sites.get(0).getFee());

			// for each camp site
			for( Campsite site: sites ) {
				result.append(String.format(linFormat,
						n+1,
						camps.get(n), 
						site.getSiteNumber(),
						site.getMaxOccupancy(),
						site.isAccessible(),
						site.getMaxRvLength(),
						site.hasUtilities(),
						strFee));
			} // for( camp site )
			if( (n+1) < camps.size() )
				result.append('\n');	

		} // for( camp ground )
		result.append(hline);

		return result.toString();
	}

	/**
	 * Create "Park Information" report.
	 * 
	 * @param park {@code Park} object containing the report data.
	 * @return Printable "Park Information" report
	 */
	public static String parkInformation(Park park) {
		final String name = park.getName() + " National Park";
		StringBuffer r = new StringBuffer();
		r.append(name); r.append('\n');
		r.append(UserIO.repeat("-", name.length())); r.append('\n');

		r.append("Location:        ");
		r.append(park.getLocation()); r.append('\n');

		r.append("Established:     ");
		r.append(park.getEstablishDate().format(dateFormat)); r.append('\n');

		r.append("Area:            ");
		r.append(integerFormat.format(park.getArea())); r.append(" sq km\n");

		r.append("Annual Visitors: ");
		r.append(integerFormat.format(park.getVisitors())); r.append("\n\n");

		r.append(park.getDescription());

		return r.toString();
	}

	/**
	 * Create a report displaying a list of camp grounds.
	 * 
	 * @param park {@code Park} object containing the report data.
	 * @return Printable "Camp Ground List" report
	 */
	public static String listCampgrounds(Park park, List<Campground> campgroundsInPark) {
		// find the length of the longest camp ground name
		int maxLengthOf = campgroundsInPark.stream()
				.mapToInt(i -> i.getName().length())
				.max().orElse(0);

		// create format command for report
		final String hdrFormat = "     %-" + maxLengthOf + "s  %-9s %-9s %9s\n";
		final String linFormat = " #%1d  %-" + maxLengthOf + "s  %-9s %-9s %9s\n";
		final String heading = String.format(hdrFormat, "Name", "Open", "Close", "Daily Fee");
		final String hline = UserIO.repeat("-", heading.length()) + "\n";

		StringBuffer result = new StringBuffer();
		result.append(hline);
		result.append(heading);
		result.append(hline);
		int menuIndex = 1;
		for( Campground camp: campgroundsInPark ) {
			result.append(String.format(linFormat, 
					menuIndex++,
					camp.getName(), 
					monthName(camp.getOpenFromMonth()),
					monthName(camp.getOpenToMonth()),
					moneyFormat.format(camp.getDailyfee())));
		}
		result.append(hline);
		return result.toString();
	} // listCampgrounds()

	/**
	 * Create a report displaying a list of camp sites.
	 * 
	 * @param park {@code Park} object containing the report data.
	 * @return Printable "Site Search List" report
	 */
	public static String listCampsitesInCampground(List<Campsite> sites) {
		// create format command for report
		final String hdrFormat = "%-8s | %-10s | %-10s | %-13s | %-7s | %8s\n";
		final String linFormat = "%-8d   %-10d   %-10s   %-13s   %-7s   %8s\n";
		final String heading = String.format(hdrFormat, "Site No.", "Max Occup.", "Accessible", "Max RV Length", "Utility", "Cost");
		final String hline = UserIO.repeat("-", heading.length()) + "\n";
		final String strFee = moneyFormat.format(sites.isEmpty()? 0: sites.get(0).getFee());

		StringBuffer result = new StringBuffer();
		result.append("\nResults Matching Your Search Criteria\n");
		result.append(hline);
		result.append(heading);
		result.append(hline);
		for( Campsite site: sites ) {
			result.append(String.format(linFormat, 
					site.getSiteNumber(),
					site.getMaxOccupancy(),
					site.isAccessible(),
					site.getMaxRvLength(),
					site.hasUtilities(),
					strFee));
		}
		result.append(hline);
		return result.toString();
	} // listCampsites()
}
