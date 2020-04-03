FROM node
WORKDIR /service
COPY package.json /service
RUN npm install
COPY . /service
CMD node index.js
EXPOSE 3000