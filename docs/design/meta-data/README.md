# Getting meta-data from the API

```bash
curl --header "Grpc-Metadata-macaroon: $(xxd -ps -u -c 1000 ./src/main/resources/tapd/admin-mainnet.macaroon)" \
--insecure https://universe.royllo.org:8089/v1/taproot-assets/assets/meta/asset-id/24a27ab522c9c33e64f4462f2acee01571e014ccbbac075786d1deae033a128d | \
jq '.data' | tr -d '"' | xxd -r -p
```

Asset meta data can be added by adding a specific request.

A menu "Add meta data" is added to the "add data" button you can find on the homepage.
A form is displayed to add the meta data with three fields:

- asset id: the asset id of the asset you want to add meta data to.
- meta data file: a file containing the meta you want to add.

When you click on the "Add meta data" button, a request is sent to the API to create the add meta data request.
If a meta data file is present, the file will be put in our storage system.

note : on an asset page, next to the meta data hash field :

- if meta data is known: a button "Get meta data" is displayed. When you click on it, a request is sent to the API to
  get the meta data of the asset.
- If meta data is not known: a button "Add meta data" is displayed. When you click on it, the form is displayed (and
  asset id is already filled).

A batch is in charge of getting all request to add meta data.
For each one, we search for the asset id in our database. If it's found, we calculate the hash of the meta data and
compare it to the one in the asset.
If it's the same, we add the file to our storage service and update the asset with the meta data file name.


