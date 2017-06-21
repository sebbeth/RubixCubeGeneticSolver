var blessed = require('blessed')
  , contrib = require('../index')

var screen = blessed.screen()

function getColourGivenInt(input) {


 switch(input) {
    case 0:
        return 'red';
        break;
    case 1:
        return 'blue';
        break;
    case 2:
        return 'yellow';
        break;
    case 3:
        return 'magenta';
        break;
    case 4:
        return 'white';
        break;
    case 5:
        return 'green';
        break;
    default:
        return 'cyan';
}


}

function generateSide(x,y,matrix,parentContainer,sideNumber) {


    var side = new blessed.box({
    top:y,
    left:x,
    width: '25%',
   height: '40%',
    tags: false
    });
    parentContainer.append(side);


    var x = 0;
    var y = 0;
    for(var i = 0; i < 9; i++){

        var tile = new blessed.box({
        top:(y * 3),
        left:(x * 5),
        width: '25%',
        height: '20%',
        tags: true,
        style: {
            fg: 'white',
            bg: getColourGivenInt(matrix[i]),
        }
        });
        side.append(tile);

        if (x >= 2) {
          x = 0;
          y++;
        } else {
            x++;
        }
    }

    var label = new blessed.box({
      top:1,
      left:20,
    width: '25%',
    height: '40%',
    tags: true,
    content: sideNumber.toString()
    });
    side.append(label);

}

/**** Data input ***/


var fs  = require("fs");
var inputArray = fs.readFileSync("output.txt").toString().split(' ');
var dataArray = new Array();

for (element of inputArray) {

  dataArray.push(parseInt(element));

}
var cubeSides = new Array();
var index = 0;
for (var i = 0; i < 6; i++ ) {


  var nextArray = new Array();

  for (var j = 0; j < 9; j++ ) {

    nextArray.push(dataArray[index]);
    index++;

  }
  cubeSides.push(nextArray);

}



/**** UI SETUP ***/


var infoBox = new blessed.box({
  top: 0,
  left: 0,
  width: '100%',
  height: '150',
  content: 'Rubix cube',
  tags: true,
  border: {
    type: 'line'
  },
 });


screen.append(infoBox);

var cubeDisplay = new blessed.box({
    top:3,
    left:0,
    width: '100%',
   height: '500',
    tags: true,

});
screen.append(cubeDisplay);

generateSide(30,0,cubeSides[0],cubeDisplay,0);
generateSide(30,12,cubeSides[1],cubeDisplay,1);
generateSide(0,12,cubeSides[2],cubeDisplay,2);
generateSide(60,12,cubeSides[3],cubeDisplay,3);
generateSide(30,24,cubeSides[4],cubeDisplay,4);
generateSide(30,36,cubeSides[5],cubeDisplay,5);




screen.key(['escape', 'q', 'C-c'], function(ch, key) {
  return process.exit(0);
});

// fixes https://github.com/yaronn/blessed-contrib/issues/10
screen.on('resize', function() {
 });

screen.render()
