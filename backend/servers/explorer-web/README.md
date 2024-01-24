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
