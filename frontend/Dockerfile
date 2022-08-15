FROM nginx:1.23-alpine
RUN mkdir -p /usr/share/nginx/html/ui
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY /dist/simple-sso-proxy/* /usr/share/nginx/html/ui/