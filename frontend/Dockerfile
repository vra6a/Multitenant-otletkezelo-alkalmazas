FROM node:latest as builder

RUN mkdir -p /app

WORKDIR /app

COPY . .

RUN npm install
RUN npm run build

EXPOSE 4200/tcp

CMD ["npm", "start"]
