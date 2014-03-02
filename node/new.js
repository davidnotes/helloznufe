var restify = require('restify');
var zmq     = require('zmq');
var events = require('events');
var db = require('./db');

var socket  = zmq.socket('req');
var server  = restify.createServer();
var i = 0;
var j = 0;
function toString(req) {
  var buffers = [];
  var data;
  req.on('data', function (trunk){
    buffers.push(trunk);
  }).on('end', function (){
  if(buffers.length > 0) {
    data = Buffer.contact(buffers);
  } else {
    data = ' ';
  }
  });
  return JSON.stringify({"method":req.method, "url":req.url, "headers":req.headers, "data":data});
}


function post(req, res, next) {
  console.log(i+" message send "+new Date().getTime());
  i++;
  socket.send(toString(req));

  socket.once("message",function(reply) {
    var test = reply.toString();
    console.log(test);
    res.write("this is test for body" + test);
    res.end();
    console.log(j+" message recv "+new Date().getTime());
    j++;
  });
  return next();
}
function get(req, res, next) {
  var sql = 'select * from courses';
  db.query(sql, function(err, result) {
    console.log(i++);
    res.write(result);
    res.end();
  });
  return next();
}
server.post('/user/1009050103/course/B100010201', post);
server.get('/course', get);
socket.connect("tcp://192.168.56.1:5555");
server.listen(8000);
