module.exports = (app, dbAdapter) => {

	app.post('/setUserSession', (req, res) => {
		dbAdapter.setUserSession(req.body.name, req.body.sessionToken, req.body.email, req.body.facebookid).then((userId) => {
			res.json({
				"status" : "success",
				"userId" : userId
			});
		}, () => {
			res.json({
				"status": "failure"
			});
		});
    });
    // app.post('/setUserData', (req, res) => {
	// 	dbAdapter.setUserData(req.body.userid, req.body.date, req.body.totalWeight, req.body.recycleWeight).then(() => {
	// 		res.json({
	// 			"status" : "success"
	// 		});
	// 	}, () => {
	// 		res.json({
	// 			"status": "failure"
	// 		});
	// 	});
    // });
}
