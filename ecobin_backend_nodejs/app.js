const express = require('express');
const app = express();
const port = process.env.PORT || 3000;
const bodyParser = require('body-parser');
const mongoClient = require('mongodb').MongoClient,
    format = require('util').format;
const mongoUrl = 'mongodb://localhost:27017/';

// mysql database connection config
const mysql = require('mysql');
const dbConfig = require('./config/database');
const connection = mysql.createConnection(dbConfig);


app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
    extended: true
}));

connection.connect((err) => {
    if (err) {
        console.log('error connecting to db');
        return;
    }
    console.log('mysql connection established');
});

// index
app.get("/", (req, res) => {
    res.send("This is the backend for ecobin!");
});

const dbAdapter = require('./adapters/dbAdapter')(connection);
//========== routes==========
require('./app/routes.js')(app, dbAdapter);

// post request to add data
app.post('/add', function(req, res, next) {
    mongoClient.connect(mongoUrl, function(err, db) {
        var dbo = db.db("test");
        if (err) {
            throw err;
        } else {
            console.log('mongodb connected');
            var dateObj = new Date(new Date().getTime() + (-8) * 3600 * 1000);
            var month = dateObj.getMonth() + 1; //months from 1-12
            var day = dateObj.getDate();
            var year = dateObj.getFullYear();

            curr_date = year + "," + month + "," + day;
            console.log(curr_date);
            var query = {
                id: req.body.id,
                "created_at": {
                    $gte: new Date(curr_date)
                }
            }
            // var new_data = { id: req.body.id, total: req.body.total, recycle: req.body.recycle , created_at: new Date(new Date().getTime() + (-8) * 3600 * 1000)};
            var newvalues = {
                $set: {
                    total: req.body.total,
                    recycle: req.body.recycle
                }
            };
            dbo.collection("data").updateOne(query, newvalues, function(err, result) {
                if (err) {
					res.send({ "status": "failure" });
                } else {
					if (result.result.n == 0) {
						console.log("insert a new one");
						var new_data = { id: req.body.id, total: req.body.total, recycle: req.body.recycle , created_at: new Date(new Date().getTime() + (-8) * 3600 * 1000)};
						dbo.collection('data').insertOne(new_data, function (err, addresult) {
			                if (err) throw err;
			                else {
								res.send({addresult});
							    console.log("insert success: id = " + req.body.id + "   total = " + req.body.total + "  recycle = "+req.body.recycle);
			                }
		            	});
					} else {
						console.log("document updated");
						// res.send({ "status": "success" });
						res.send({result});
					}
                }
                db.close();
            });
        }
    });
});

// post query
app.post('/find_by_time', function(req, res, next) {
    mongoClient.connect(mongoUrl, function(err, db) {
        var dbo = db.db("test");
        if (err) {
            throw err;
        } else {
            console.log('mongodb connected');
            console.log(req.body.id);
            var query = {
                "created_at": {
                    $gte: new Date(req.body.time)
                },
                id: req.body.id
            }
            dbo.collection("data").find(query).toArray(function(err, result) {
                if (err) {
                    res.send({
                        "status": "failure"
                    });
                } else {
                    console.log(result);
                    db.close();
                    res.send(result);
                }

            });
        }
    });
});

app.listen(port, (err) => {
    if (err) {
        return console.log('something bad happened', err);
    }

    console.log(`server listening on ${port}`);
})
