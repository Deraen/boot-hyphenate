(set-env!
  :resource-paths #{"src"}
  :dependencies   '[[org.clojure/clojure  "1.7.0"  :scope "provided"]
                    [boot/core            "2.3.0"  :scope "provided"]
                    [adzerk/bootlaces     "0.1.11" :scope "test"]
                    [deraen/clj-hyphenate "0.1.0"  :scope "test"]
                    [enlive               "1.1.6"  :scope "test"]])

(require '[adzerk.bootlaces :refer :all])

(def +version+ "0.1.0")

(bootlaces! +version+)

(task-options!
  pom {:project     'deraen/boot-hyphenate
       :version     +version+
       :description "Boot task to hyphenate html files"
       :url         "https://github.com/deraen/boot-hyphneate"
       :scm         {:url "https://github.com/deraen/boot-hyphneate"}
       :license     {"MIT" "http://opensource.org/licenses/mit-license.php"}})

(deftask dev
  "Dev process"
  []
  (comp
    (watch)
    (repl :server true)
    (build-jar)))
