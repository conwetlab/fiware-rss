#!/bin/bash

if [ -z "$RSS_CLIENT_ID" ]; then
    echo "RSS_CLIENT_ID not defined"
    exit 1
fi

if [ -z "$RSS_SECRET" ]; then
    echo "RSS_SECRET not defined"
    exit 1
fi

if [ -z "$RSS_URL" ]; then
    echo "RSS_URL not defined"
    exit 1
fi

sed -i "s/config.client_id=.*$/config.client_id=$RSS_CLIENT_ID/" /etc/default/rss/oauth.properties
sed -i "s/config.client_secret=.*$/config.client_secret=$RSS_SECRET/" /etc/default/rss/oauth.properties
sed -i "s|config.callbackURL=.*$|config.callbackURL=$RSS_URL/fiware-rss/callback|" /etc/default/rss/oauth.properties

# Waiting for DB
sleep 15

exec "$@"
