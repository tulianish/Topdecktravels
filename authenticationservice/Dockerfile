FROM node
WORKDIR /service
COPY package.json /service
RUN npm install
COPY . /service
CMD node server.js
EXPOSE 3001