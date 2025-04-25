# ClockWise_Application
ClockWise is an attendance tracking application that features seamless event management and accurate real-time data tracking for attendees. It offers various check-in options such as QR code scanning and geolocation verification, providing flexibility for a wide range of events. With ClockWise, Event Makers can efficiently manage and track attendance effortlessly while ensuring a smooth experience for participants.

## Service and APIs
● Geolocation Maps (Google Play Services Location API):

> Monitors current location of user within a set boundary in a certain location (i.e. classroom) to
determine attendance.

● Service (Background Running):

> Tracks user’s location even if the app is closed or inactive.

● Local Database (Firebase):

> Stores and tracks attendance of users remotely. Gathers the necessary data using a broadcast receive

## Functions
| Functions |  Description  |
|:-----|:--------:|
| **Register**   | The user must first register an account before accessing the app. Event Makers need to provide full name, email, and organization info, while Attendees provide basic details like full name and email. |
| Log-in   |  The user must log in before accessing their dashboard. Event Makers and Attendees will log in using their registered email and pass  |
| Create Event   | Event Makers can create an event with a specific date, time, and location. A unique QR code will be generated for each event, and a Google Sheet will be created to track attendance |
| Customize Attendee Form   | Event Makers can customize the information they require from attendees (e.g., full name, contact info, ID number, etc.), similar to a Google Form setup. |
| Generate QR Code   | Event Makers generate QR codes for each event, which attendees scan to log their attendance. |
| Scan QR Code & Google Sheets Sync   | When attendees scan the QR code, their details (as specified by the Event Maker in the customized form) are automatically added to the Event Maker's Google Sheet in real-time. |
| View Attendance Records   | Event Makers can view real-time data of attendee check-ins, including names, check-in times, and locations, which are stored in their Google Sheet. |
| Attendance Reports   | Event Makers can generate attendance reports from the Google Sheet, showing who attended, who was late, and who missed the event. |
| Program Notification   | Event Makers and Attendees will receive notifications before the event begins, reminding them of the event schedule. |
| Geolocation Verification   | The app checks the attendee’s location to verify if they are at the designated location during check-in. |
| Offline Mode   | Attendees can scan QR codes to check in even when offline. The app stores data locally and syncs when internet connectivity is restored. |

<h2>💌 Credits ✉️</h2>
This project is done by <b>ERMITANO, Kate Justine, LLENAREZ, Gabrielle</b>, and <b>VELASQUEZ, Almira</b> as a requirement to pass CSOPESY under the instructions of <b>Sir Joel Andrew Jr. Cruz</b>, submitted on December 7, 2024.
