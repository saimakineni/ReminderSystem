const functions = require("firebase-functions");
const admin = require("firebase-admin");
admin.initializeApp();
const database = admin.database();

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions

exports.scheduledFunction = functions.pubsub.schedule("every 1 minutes")
    .onRun((context) => {
      database.ref("Reminders/").once("value", function(snapshot) {
        const data = snapshot.val();
        const keys = Object.keys(data);
        for (let k=0; k<keys.length; k++) {
          const i = keys[k];
          const ts = Object.keys(data[i]);
          for (let j=0; j<ts.length; j++) {
            const a = ts[j];
            const message = data[i][a].message;
            let date = data[i][a].date;
            let time = data[i][a].time;
            const receipents = data[i][a].receipnts;
            // const services = data[i][a].services;
            const today = new Date();
            const PM = time.match("PM") ? true : false;
            time = time.split(":");
            let rhour;
            let rmin;
            if (PM) {
              rhour = 12 + parseInt(time[0], 10);
              rmin = time[1].replace("PM", "");
            } else {
              rhour = time[0];
              rmin = time[1].replace("AM", "");
            }
            // console.log(date, "    ", rmin);
            date = date.split("/");
            // console.log("Reminder date", date[2], date[0], date[1]);
            const RDate = new Date(date[2], date[0]-1, date[1],
                rhour, rmin);
            const day = today.getDate();
            const month = today.getMonth();
            const year = today.getFullYear();
            const hr = today.getHours();
            const min = today.getMinutes();
            const CDate = new Date(year, month, day, hr-4, min);
            console.log("difference", (RDate.getTime()-CDate.getTime())/1000);
            console.log("Currenttt Time", CDate);
            console.log("Reminder Time", RDate);
            if ((RDate.getTime()-CDate.getTime())/1000 <= 120 &&
             (RDate.getTime()-CDate.getTime())/1000 > 0) {
              sendSms(message, receipents);
              sendMessage(message, receipents);
            } else {
              console.log("Lot of time to go!");
            }
          }
        }
      });
      return console.log("This will be run every 5 minutes!");
    });

/**
 * Adds two numbers together.
 * @param {string} message The first number.
 * @param {array} receipents The second number.
 */
function sendSms(message, receipents) {
  console.log("In SMS function");
  const accountSid = "AC4b899f847d2b53dfc679b522ed3f4281";
  const authToken = "5506fbe931dfc4d9fd7896e6a783f271";
  const client = require("twilio")(accountSid, authToken);
  for (let i=0; i<receipents.length; i++) {
    if (receipents[i].length== 10) {
      client.messages.create({body: message, from: "+14402615062",
        to: "+1"+receipents[i]})
          .then((message) => console.log(message.sid));
    }
  }
}

/**
 * Adds two numbers together.
 * @param {string} message The first number.
 * @param {array} receipents The second number.
 */
function sendMessage(message, receipents) {
  console.log("In Discord");
  const Discord = require("discord.js");
  for (let i=0; i<receipents.length; i++) {
    if (receipents[i].length>10) {
      const url = receipents[i].split("/");
      const len = url.length;
      console.log(url[len-2]);
      console.log(url[len-1]);
      const webhookClient = new Discord.WebhookClient(url[len-2], url[len-1]);
      const embed = new Discord.MessageEmbed().setTitle(message)
          .setColor("#0099ff");
      webhookClient.send("", {
        username: "Spidey Bot",
        avatarURL: "",
        embeds: [embed],
      });
    }
  }
}
