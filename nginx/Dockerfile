#FROM nginx:alpine
#
#RUN apk add --no-cache certbot certbot-nginx
#
#COPY nginx.conf /etc/nginx/nginx.conf
#
#
#COPY /nginx/nginx.conf /etc/nginx/conf.d/default.conf
#
#EXPOSE 80 443
## Start Nginx and ensure Certbot runs at startup
#CMD ["sh", "-c", "nginx -g 'daemon off;'"]

FROM nginx:latest

COPY /nginx/nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
