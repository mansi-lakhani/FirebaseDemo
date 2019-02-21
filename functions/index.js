const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);


//function to write on particular positio in database
exports.pushNotification = functions.database.ref('/Booking/{pushId}').onWrite( ( change,context) => {

	console.log('Push notification event triggered');
	/* Grab the current value of what was written to the Realtime
	Database.*/
	var valueObject = change.after.val();


	const payload = {
	        notification: {
	            title: valueObject.userid,
	            body: "Created a booking",
	            sound: "default",
	            click_action: "android.intent.action.MAIN"
		},
	};


//Create an options object that contains the time to live for the notification and the priority
    const options = {
        priority: "high",
        timeToLive: 60 * 60 * 24
    };

     return admin.messaging().sendToTopic("pushNotifications", payload, options);
});
