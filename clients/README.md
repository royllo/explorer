# Web client

## Install development tools

### Node.js

```
curl -fsSL https://deb.nodesource.com/setup_18.x | sudo -E bash -
sudo apt purge nodejs
sudo apt autoremove
sudo apt-get install -y nodejs
sudo npm install --global yarn
```

You can also install :

- [Chrome developer plugin](https://chrome.google.com/webstore/detail/vuejs-devtools/nhdogjmejiglipccpnnnanhbledajbpd)
- [Intellij Taiwlind plugin](https://plugins.jetbrains.com/plugin/15321-tailwind-css)

## Run the project

In `/clients/web`, juste type `yarn dev -o`

## Project creation

Of course, you don't have to do this but I wrote it down to keep it in mind

```
npx nuxi init web
cd web
npm install
yarn add --dev @nuxtjs/tailwindcss
```

TODO : How to use npm install @tailwindcss/typography ?
