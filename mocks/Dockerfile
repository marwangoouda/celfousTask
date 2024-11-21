FROM node:21-alpine

WORKDIR /app

COPY apis-mock.json .
COPY routes.json .

RUN npm install -g json-server@v0.17.4

EXPOSE 8082

CMD ["json-server", "-H", "0.0.0.0", "--watch", "apis-mock.json", "--routes", "routes.json", "--port", "8082"]