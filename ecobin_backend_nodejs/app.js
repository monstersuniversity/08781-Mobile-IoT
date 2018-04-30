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

// post query find today's data
app.post('/find_by_time', function(req, res, next) {
    mongoClient.connect(mongoUrl, function(err, db) {
        var dbo = db.db("test");
        if (err) {
            throw err;
        } else {
            console.log('mongodb connected');
            var query = {
                "created_at": {
                    $gte: new Date(req.body.time),
                },
                id: req.body.id
            }
            dbo.collection("data").find(query).toArray(function(err, result) {
                if (err) {
                    res.send({
                        "status": "failure"
                    });
                } else {
					if (result.length == 0) {
						res.send({
							"answer" : 0
						});
					} else {
						res.send({
							"answer" : result[0].recycle / result[0].total * 100
						});
					}

                    db.close();
                }

            });
        }
    });
});

// post query find last n weeks' data
app.post('/find_by_week', function(req, res, next) {
    mongoClient.connect(mongoUrl, function(err, db) {
        var dbo = db.db("test");
        if (err) {
            throw err;
        } else {
            console.log('mongodb connected');
			var dateObj = new Date(new Date().getTime() + (-8) * 3600 * 1000);
			dateObj.setDate(dateObj.getDate() - req.body.n + 7);
			var old_dateObj = new Date(new Date().getTime() + (-8) * 3600 * 1000);
			old_dateObj.setDate(old_dateObj.getDate() - req.body.n);
			var month = dateObj.getMonth() + 1;
			var day = dateObj.getDate();
			var year = dateObj.getFullYear();

			var old_month = old_dateObj.getMonth() + 1;
			var old_day = old_dateObj.getDate();
			var old_year = old_dateObj.getFullYear();

			curr_date = year + "," + month + "," + day;
			prev_date = old_year + "," + old_month + "," + old_day;
			console.log(curr_date);
			console.log(prev_date);
            var query = {
                "created_at": {
                    $lt: new Date(curr_date),
					$gte: new Date(prev_date),
                },
                id: req.body.id
            }
            dbo.collection("data").find(query).toArray(function(err, result) {
                if (err) {
                    res.send({
                        "status": "failure"
                    });
                } else {
					var totalPercent = 0;
					for (var i = 0; i < result.length; i++) {
						totalPercent = totalPercent + result[i].recycle / result[i].total;
					}
                    db.close();
                    res.send({
						"answer" : totalPercent / result.length * 100
					});
                }
            });
        }
    });
});

// post query find by month last year
app.post('/find_by_month', function(req, res, next) {
    mongoClient.connect(mongoUrl, function(err, db) {
        var dbo = db.db("test");
        if (err) {
            throw err;
        } else {
            console.log('mongodb connected');
			start_date = req.body.year + "," + req.body.smonth;
			end_date = req.body.year + "," + req.body.emonth;
			console.log(new Date(start_date));
			console.log(new Date(end_date));
            var query = {
                "created_at": {
                    $lt: new Date(end_date),
					$gte: new Date(start_date),
                },
                id: req.body.id
            }
            dbo.collection("data").find(query).toArray(function(err, result) {
                if (err) {
                    res.send({
                        "status": "failure"
                    });
                } else {
					var totalPercent = 0;
					for (var i = 0; i < result.length; i++) {
						totalPercent = totalPercent + result[i].recycle / result[i].total;
					}
                    db.close();
                    res.send({
						"answer" : totalPercent / result.length * 100
					});
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
