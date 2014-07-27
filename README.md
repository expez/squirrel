# squirrel

Often, when writing tests, I find myself calling some production services or accessing a db to get some authentic data for use in my tests.  Then I create mocks which returns the previously collected data.

Squirrel helps you with

1. Squirrelling away values, by instrumenting functions and persisting the input => output pairs.
2. Easily creating mocks with previously recorded canned responses.

## Usage

We want to test foo, but at the very bottom foo relies on bar and baz which call some external services.  In the repl we do:
```clj
(recording [bar baz] "test/resources/nuts.edn"
  (foo user-id))
```

This will record the observed input => output pairs for the functions `bar` and `baz` into `test/resources/nuts.edn` which we can use in our tests like this:

```clj
(deftest foo-does-what-it-should
  (with-recordings "test/resources/nuts.edn"
    (is (= (foo user-id) {:something "or other"}))))
```
## License

Copyright © 2014 Lars Andersen

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
