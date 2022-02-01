1. Install Chrome Plugin "Rabbit URL Rewriter":
- https://chrome.google.com/webstore/detail/rabbit-url-rewriter/kcbmcmeblpkcndhfhkclggekfblookii?hl=en

2. Create 1 group (e.g. "geobooster") with the following 2 redirect-rules:
- https://pflege-novap-(.*).sbb-cloud.net/geo/wms(.*) -> http://localhost:8084/geo/wms$2
- https://pflege-ui-novap-(.*).sbb-cloud.net/geo/wms(.*) -> http://localhost:8084/geo/wms$2

3. Unzip & start GeoBooster

4. Select DB & load DR (should take about 1min)

5. Open any Pflege GUI with a map & enjoy the speed :)


Troubleshooting:
- If the program doesn't start, make sure a) the .properties file is in the same directory as the .exe & b) port 8084 is not in use
- If the DR loading fails (sudden connection drop): edit file geo-booster.properties and reduce datasource.fetchSize to 1000
- If the loading with reduced fetchSize takes a very long time (>5min, e.g. in home office with slow ), try datasource.useJsonAgg = true
