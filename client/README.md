# VAIM Client source code

This code is supplied as supplementary material for the TVCG paper submission "Influence Maximization with Visual Analytics". What follows is the README file of the client code.

## Requirements

* npm for building 
* VAIM server running

## Install

To build it, get into the root folder of the project and type the following:

```
$ npm install 
```

It might take a few minutes, depending on the network speed and computer capabilities.

## Start or Build

To start the client in development mode, run the following:

```
$ npm start
```

To build the client for deployment, first add the ``"homepage"`` directive onto ``package.json``. Afterwards, run ``npm build`` and then move the contents of the ``build`` folder onto the web server folder.

### Configuration

In case you changed the address of the server from the default, change it in ``utils/dataLoder.js`` on line 19:

```
const serverRoot = "http://localhost:8088/VAIM/";
```

## Known Issues

If the client gets stuck on waiting for a server response, check the server output log and restart it if necessary.

## Notes

This project was bootstrapped with [Create React App](https://github.com/facebook/create-react-app).

