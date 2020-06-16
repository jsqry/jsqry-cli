# jsqry-cli

Command-line jsqry (like jq)

```bash
$ echo '[{"a":1},{"a":2}]' | jsqry 'a' # query
[1, 2]

$ echo '[{"a":1},{"a":2}]' | jsqry -1 'a' # first
1
```

The output is pretty-printed by default.