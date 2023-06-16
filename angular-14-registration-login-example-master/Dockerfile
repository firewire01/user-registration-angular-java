FROM node:alpine AS builder
WORKDIR /app
COPY . .
RUN npm install && \
    npm run build
FROM nginx:alpine
COPY --from=builder /app/dist/* /usr/share/nginx/html/
COPY /nginx.conf /etc/nginx/conf.d/default.conf
 
 
# Expose port 80 to the Docker host, so we can access it 
# from the outside.
EXPOSE 80
 
ENTRYPOINT ["nginx","-g","daemon off;"]