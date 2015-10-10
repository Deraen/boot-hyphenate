# boot-hyphenate
[![Clojars Project](http://clojars.org/deraen/boot-hyphenate/latest-version.svg)](http://clojars.org/deraen/boot-hyphenate)

[Boot](https://github.com/boot-clj/boot) task to hyphenate HTML files using
[clj-hyphenate](https://github.com/Deraen/clj-hyphenate).

## Use

The task will replace contents of HTML text nodes (`p`, `h1-6`, ...) with
text which contains soft-hyphens (`&shy;`). To tell browsers to use these
soft-hyphens use following CSS:

```css
p {
  hyphens: manual;
  /* Optional */
  text-align: justify;
}
```

## License

Copyright (C) 2015 Juho Teperi

Distributed under the MIT License.
