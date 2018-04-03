const express = require('express');
const app = express();
const port = process.env.PORT || 3000;
const bodyParser = require('body-parser');
const mongoClient = require('mongodb').MongoClient,format=require('util').format;
const mongoUrl = 'mongodb://localhost:27017/';

// mysql database connection config
const mysql = require('mysql');
const dbConfig = require('./config/database');
const connection = mysql.createConnection(dbConfig);


app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended:true}));

connection.connect((err) => {
	if(err){
		console.log('error connecting to db');
		return;
	}
	console.log('mysql connection established');
});

// index
app.get("/", (req, res) => {
    res.send("Hello World");
});

const dbAdapter = require('./adapters/dbAdapter')(connection);
//========== routes==========
require('./app/routes.js')(app, dbAdapter);

// post request to add data
app.post('/add', function(req, res, next) {
    mongoClient.connect(mongoUrl, function(err, db) {
        var dbo = db.db("test");
        if(err){
            throw err;
        } else {
            console.log('mongodb connected');
            var new_data = { id: req.body.id, total: req.body.total, recycle: req.body.recycle , created_at: new Date()};
            dbo.collection('data').insertOne(new_data, function (err, result) {
                if (err) {
                    res.json({
                        "status" : "failure"
                    });
                }
                else {
                    res.json({
                        "status" : "success"
                    });
                  console.log("insert success: id = " + req.body.id + "   total = " + req.body.total + "  recycle = "+req.body.recycle);
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
        if(err){
            throw err;
        } else {
            console.log('mongodb connected');
            console.log(req.body.id);
            var query = {"created_at" : { $gte : new Date(req.body.time)}, id : req.body.id}
            dbo.collection("data").find(query).toArray(function(err, result) {
                if (err) {
                    res.json({
                        "status" : "failure"
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
	if(err) {
		return console.log('something bad happened', err);
	}

	console.log(`server listening on ${port}`);
})
