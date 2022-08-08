# VAIM Client source code

## Requirements

* [NPM](https://www.npmjs.com/) package manager for building (on Windows, recommended portable installation or the use of a [Node version manager](https://npm.github.io/installation-setup-docs/installing/using-a-node-version-manager.html))
* VAIM server running

## Installation

Installation can be done automatically with the install script in the ```scripts``` directory in the root of this repository or manually as follows.

In the following, command line statements work interchangeably on both Windows and Linux unless explicitly stated otherwise. They also assume the working directory to be ```/path/to/VAIM/client```.

To install, type the following:

```
npm install 
```

It might take a few minutes, depending on the network speed and computer capabilities.

## Start or Build

To start the client in development mode, run the following:

```
npm start
```

To build the client for deployment, first add the ```homepage``` directive onto ```package.json``` (please refer to the [official documentation](https://docs.npmjs.com/cli/v8/configuring-npm/package-json)). Afterwards, run ```npm build``` and then move the contents of the ```build``` folder onto the web server folder.

### Configuration

In case you want to build the client for deployment, change the server address in ``utils/dataLoder.js`` on line 19:

```
const serverRoot = "http://localhost:8088/VAIM/";
```

## Known Issues

If the client gets stuck on waiting for a server response, check the server output log and restart it if necessary.

## Notes

This project was bootstrapped with [Create React App](https://github.com/facebook/create-react-app).

