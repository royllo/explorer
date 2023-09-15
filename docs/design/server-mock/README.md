# Mock server

To simplify our tests, we developed a mock server that can be used with JUnit.

## Key concept

- We are using [MockServer](https://www.mock-server.com/) to mock the rest replies.
- For mempool or tapd, we are reading a directory of files containing the service result.
- MockServer is started for every tests using it.
- When it starts, it displays its configuration to make it easier to understand errors.

## Mempool

We will search for a folder named `mock-server/mempool/` containing files named after the mempool request.
The json file in this directory will have as name the transaction id and will contain the mempool response.
Example: `mock-server/mempool/b97285f17dc029b92dfe0a5c9f2be412b13699fe0cf93f99deb606b20b110e75.json`

You can create a new file by using:

## Tapd

All folders abouts assets will be in a folder named `mock-server/tapd/`:

### Decode

- Decoder responses are in `mock-server/tapd/decode`.
- The folder will be named after the asset name. This is just to make it easier to maintaining the files, the directory
  name won't be used.
- In an asset folder, we will have several files:
    - `raw_proof_file_I` (where I is the i-th proof file, example `raw_proof_file_1`).
    - `raw_proof_file_I_decode_J.json` (where J is the j-th decode of the i-th proof file,
      example `raw_proof_file_1_decode_1.json`).
    - If decode is not specified we, return the decode file ending with decode_0.json.