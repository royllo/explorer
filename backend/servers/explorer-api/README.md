# Explorer api

## How to run dev environment ?

Server side:
`mvn spring-boot:run -Dspring-boot.run.profiles=dev`

You can then access graphiql here: [http://localhost:8080/graphiql](http://localhost:8080/graphiql).

Here are some queries you can use:

```
{
  userByUsername(username: "anonymous") {
    userId
    username
  }
}
```

You can call them with curl:

```bash
curl    -H "Content-Type: application/json" \
        -i \
        -X GET \
        -d '{ "query": "{ userByUsername(username: \"anonymous\") { userId username } }" }' \
        http://localhost:8080/graphql
```

```
{
  queryAssets(query: "coin") {
    content {
      assetId
      name
    }
  }
}
```
