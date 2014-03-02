var mysql = require('mysql');
var pool = mysql.createPool({
  host : 'localhost',
  database: 'test4lazycat',
  user : 'root',
  password : 'r00t',
  connectionLimit : 100
});

exports.query = function(sql, callback) {
  var result = "{[";
  pool.getConnection(function(err, connection) {
    var query = connection.query(sql);
    query.on('error', function(err) {
      return callback(err);
    }).on('fields', function(fields) {
    }).on('result', function(row) {
      connection.pause();
      result = result + JSON.stringify(row) + ',';
      connection.resume();
    }).on('end', function() {
      result = result + ']}';
      connection.release();
      callback(null, result);
    });
  });
};