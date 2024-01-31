# Explorer web

## How to run dev environment ?

Server side:
`mvn spring-boot:run -Dspring-boot.run.profiles=dev`

Client side:
`npm run build && npm run watch`

## Lnurl Auth

In order for lnurl-auth to work you must serve your app over `https` (no self-signed cert allowed).

Serving your app with `https` during development can be done with [ngrok](https://ngrok.com/):

```bash
./ngrok http 8080
```

This will return a public url that you can use to access your app over `https`:

```bash
Session Status                online                                                                                                                                                                        
Account                       stephane.traumat@gmail.com (Plan: Free)                                                                                                                                       
Version                       3.5.0                                                                                                                                                                         
Region                        Europe (eu)                                                                                                                                                                   
Latency                       22ms                                                                                                                                                                          
Web Interface                 http://127.0.0.1:4040                                                                                                                                                         
Forwarding                    https://348a-2001-861-5300-9e20-bf7e-7941-39d8-1e6d.ngrok-free.app -> http://localhost:8080
```

You can then use the `https` url to access your app by updating `application-dev.properties`, change
to `royllo.explorer.web.base-url=https://348a-2001-861-5300-9e20-bf7e-7941-39d8-1e6d.ngrok-free.app` (without the
trailing `/`).

Then run `mvn spring-boot:run -Dspring-boot.run.profiles=dev`.s