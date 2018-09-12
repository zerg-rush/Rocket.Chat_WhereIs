const jsonServer = require('json-server');
const server = jsonServer.create();
const router = jsonServer.router('db.json');
const middlewares = jsonServer.defaults();

const moment = require('moment');

const names = ['Adam', 'Tomasz', 'Marek', 'Jakub', 'John', 'Adrian', 'Bartosz', 'Maciej', 'Donald'];
const surnames = ['Kowalski', 'Malinowski', 'Nowak', 'Iksinski', 'Smith', 'Doe', 'Musk', 'Trump'];

server.use(middlewares);

function isNumeric(n) {
    return !isNaN(parseFloat(n)) && isFinite(n)
}

function generateRandomBetween(min, max) {
    if (isNumeric(min) && isNumeric(max)) {
        return Math.floor(Math.random() * parseFloat(max - min)) + parseFloat(min)
    }
}

function generateRandomIP() {
    return '192.168.1.' + generateRandomBetween(2, 254);
}

function generateRandomMAC() {
    return '' +
        generateRandomBetween(1, 9) + generateRandomBetween(0, 9) + '-' +
        generateRandomBetween(1, 9) + generateRandomBetween(0, 9) + '-' +
        generateRandomBetween(1, 9) + generateRandomBetween(0, 9) + '-' +
        generateRandomBetween(1, 9) + generateRandomBetween(0, 9) + '-' +
        generateRandomBetween(1, 9) + generateRandomBetween(0, 9) + '-' +
        generateRandomBetween(1, 9) + generateRandomBetween(0, 9);
}

function generateRandomBoolean() {
    return Math.random() > 0.5;
}

function generateRandomNetType() {
    return (generateRandomBoolean() ? 'WIFI' : 'LAN');
}

function generateRandomStatus() {
    return (generateRandomBoolean() ? 'ONLINE' : (generateRandomBoolean() ? 'AWAY' : 'OFFLINE'));
}

function generateRandomLastActivityTime() {
    const now = new Date();
    const hours = generateRandomBetween(0, 3);
    const minutes = generateRandomBetween(0, 59);
    const seconds = generateRandomBetween(0, 59);

    return moment()
        .subtract(hours, 'hours')
        .subtract(now.getTimezoneOffset() + minutes, 'minutes')
        .subtract(seconds, 'seconds')
        .format('YYYY-MM-DD HH:mm:ss');
}

function generateRandomLocation(mode, id) {
    const randomLocationNumber = generateRandomBetween(1, 18);
    const randomNetType = generateRandomNetType();
    const randomStatus = generateRandomStatus();
    const randomIP = generateRandomIP();
    const randomMAC = generateRandomMAC();
    const randomLastActivityTime = generateRandomLastActivityTime();

    let response;
    if (randomNetType === 'WIFI') {
        response = {
            index: randomNetType + '_10' + (randomLocationNumber >= 10 ? randomLocationNumber : '0' + randomLocationNumber),
            type: randomNetType,
            lastActivity: randomLastActivityTime,
            status: randomStatus,
            signalStrength: generateRandomBetween(1, 10),
            ip: (mode === 'ip' ? id : randomIP),
            mac: (mode === 'mac' ? id : randomMAC)
        }
    } else {
        response = {
            index: randomNetType + '_10' + (randomLocationNumber >= 10 ? randomLocationNumber : '0' + randomLocationNumber),
            type: randomNetType,
            lastActivity: randomLastActivityTime,
            status: randomStatus,
            ip: (mode === 'ip' ? id : randomIP),
            mac: (mode === 'mac' ? id : randomMAC)
        }
    }

    return response;
}

function generateRandomLocationsArray(mode, id) {
    let locationsNumber = 1;
    if (mode === 'user' && Math.random() > 0.8) locationsNumber = 2;    // 20% cases with at least two locations
    if (mode === 'user' && Math.random() > 0.9) locationsNumber = 3;    // 10% cases with three locations
    let locationsArray = [];

    for (let i = 0; i < locationsNumber; i++) {
        locationsArray.push(generateRandomLocation(mode, id))
    }

    return locationsArray
}

function generateRandomFullname() {
    const nameIndex = generateRandomBetween(0, names.length - 1);
    const surnameIndex = generateRandomBetween(0, surnames.length - 1);
    return names[nameIndex] + ' ' + surnames[surnameIndex];
}

function randomizeMiddleware(req, res) {
    const randomFullname = generateRandomFullname();
    let id = req.url.replace('/', '');
    let mode;
    if (id.includes('.')) {
        mode = 'ip';
    } else {
        if (id.includes('-') || id.includes(':')) {
            mode = 'mac';
        } else {
            mode = 'user';
        }
    }
    let randomArray = generateRandomLocationsArray(mode, id);

    let randomResponse =
        {
            id: req.url.replace('/', ''),
            fullname: randomFullname,
            locations: randomArray
        };
    res.jsonp(randomResponse);
    /*console.log('Response:');
    console.log(randomResponse);*/
}

server.use('/whereis/', randomizeMiddleware);
server.use(router);

server.use(function (req, res) {
    res.status(404).send({
        "error": "can't find what you are looking for"
    })
});

server.listen(8081, function () {
//    console.log('Ready')
});
