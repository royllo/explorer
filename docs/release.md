# Release

You must be using ssh and not https. To switch to ssh, type :

```bash
git remote set-url origin git@github.com:royllo/explorer.git
```

Check that you are on the development branch and that everything is committed:

```bash
git checkout development
git status
```

Then, Start the release with:

```bash
mvn gitflow:release-start
```

After choosing the release number, finish the release, push branches and tags, with this command:

```bash
mvn gitflow:release-finish
```