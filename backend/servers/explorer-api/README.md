# Explorer api

## How to run dev environment ?

Server side:
`mvn spring-boot:run -Dspring-boot.run.profiles=dev`

You can then access graphiql here: [http://localhost:8080/graphiql](http://localhost:8080/graphiql).

Here are some queries you can use:
```
{
  userByUsername(username: "anonymous") {
    id
    username
  }
}
```

```
{
  queryAssets(query: "coin") {
    content {
      id
      name
      assetId
    }
  }
}
```
