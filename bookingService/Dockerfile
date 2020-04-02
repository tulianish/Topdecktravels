FROM node
WORKDIR /service
COPY package.json /service
RUN npm install
COPY . /service
CMD node app.js
EXPOSE 3002