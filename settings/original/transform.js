// using node library
const fs = require('fs');

var jsonFile = fs.readFileSync('./sample.json', 'utf8');
var jsonData = JSON.parse(jsonFile);

console.log(jsonData.data);