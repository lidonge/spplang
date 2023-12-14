//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.samskivert.mustache;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public abstract class BasicCollector implements Mustache.Collector {
    protected static final Mustache.VariableFetcher CUSTOM_FETCHER = new Mustache.VariableFetcher() {
        public Object get(Object ctx, String name) throws Exception {
            Mustache.CustomContext custom = (Mustache.CustomContext)ctx;
            Object val = custom.get(name);
            return val == null ? Template.NO_FETCHER_FOUND : val;
        }

        public String toString() {
            return "CUSTOM_FETCHER";
        }
    };
    protected static final Mustache.VariableFetcher MAP_FETCHER = new Mustache.VariableFetcher() {
        public Object get(Object ctx, String name) throws Exception {
            Map<?, ?> map = (Map)ctx;
            if (map.containsKey(name)) {
                return map.get(name);
            } else {
                return "entrySet".equals(name) ? map.entrySet() : Template.NO_FETCHER_FOUND;
            }
        }

        public String toString() {
            return "MAP_FETCHER";
        }
    };
    protected static final Mustache.VariableFetcher LIST_FETCHER = new Mustache.VariableFetcher() {
        public Object get(Object ctx, String name) throws Exception {
            try {
                return ((List)ctx).get(Integer.parseInt(name));
            } catch (NumberFormatException var4) {
                return Template.NO_FETCHER_FOUND;
            } catch (IndexOutOfBoundsException var5) {
                return Template.NO_FETCHER_FOUND;
            }
        }

        public String toString() {
            return "LIST_FETCHER";
        }
    };
    protected static final Mustache.VariableFetcher ITER_FETCHER = new Mustache.VariableFetcher() {
        public Object get(Object ctx, String name) throws Exception {
            try {
                Iterator<?> iter = (Iterator)ctx;
                int ii = 0;

                for(int ll = Integer.parseInt(name); ii < ll; ++ii) {
                    iter.next();
                }

                return iter.next();
            } catch (NumberFormatException var6) {
                return Template.NO_FETCHER_FOUND;
            } catch (NoSuchElementException var7) {
                return Template.NO_FETCHER_FOUND;
            }
        }

        public String toString() {
            return "ITER_FETCHER";
        }
    };
    protected static final ArrayHelper OBJECT_ARRAY_HELPER = new ArrayHelper() {
        protected Object get(Object ctx, int index) {
            return ((Object[])ctx)[index];
        }

        public int length(Object ctx) {
            return ((Object[])ctx).length;
        }
    };
    protected static final ArrayHelper BOOLEAN_ARRAY_HELPER = new ArrayHelper() {
        protected Object get(Object ctx, int index) {
            return ((boolean[])ctx)[index];
        }

        public int length(Object ctx) {
            return ((boolean[])ctx).length;
        }
    };
    protected static final ArrayHelper BYTE_ARRAY_HELPER = new ArrayHelper() {
        protected Object get(Object ctx, int index) {
            return ((byte[])ctx)[index];
        }

        public int length(Object ctx) {
            return ((byte[])ctx).length;
        }
    };
    protected static final ArrayHelper CHAR_ARRAY_HELPER = new ArrayHelper() {
        protected Object get(Object ctx, int index) {
            return ((char[])ctx)[index];
        }

        public int length(Object ctx) {
            return ((char[])ctx).length;
        }
    };
    protected static final ArrayHelper SHORT_ARRAY_HELPER = new ArrayHelper() {
        protected Object get(Object ctx, int index) {
            return ((short[])ctx)[index];
        }

        public int length(Object ctx) {
            return ((short[])ctx).length;
        }
    };
    protected static final ArrayHelper INT_ARRAY_HELPER = new ArrayHelper() {
        protected Object get(Object ctx, int index) {
            return ((int[])ctx)[index];
        }

        public int length(Object ctx) {
            return ((int[])ctx).length;
        }
    };
    protected static final ArrayHelper LONG_ARRAY_HELPER = new ArrayHelper() {
        protected Object get(Object ctx, int index) {
            return ((long[])ctx)[index];
        }

        public int length(Object ctx) {
            return ((long[])ctx).length;
        }
    };
    protected static final ArrayHelper FLOAT_ARRAY_HELPER = new ArrayHelper() {
        protected Object get(Object ctx, int index) {
            return ((float[])ctx)[index];
        }

        public int length(Object ctx) {
            return ((float[])ctx).length;
        }
    };
    protected static final ArrayHelper DOUBLE_ARRAY_HELPER = new ArrayHelper() {
        protected Object get(Object ctx, int index) {
            return ((double[])ctx)[index];
        }

        public int length(Object ctx) {
            return ((double[])ctx).length;
        }
    };

    public BasicCollector() {
    }

    public Iterator<?> toIterator(final Object value) {
        if(value instanceof Map<?,?>){
            return ((Map<?, ?>) value).entrySet().iterator();
        }else if (value instanceof Iterable) {
            return ((Iterable)value).iterator();
        } else if (value instanceof Iterator) {
            return (Iterator)value;
        } else if (value.getClass().isArray()) {
            final ArrayHelper helper = arrayHelper(value);
            return new Iterator<Object>() {
                private int _count = helper.length(value);
                private int _idx;

                public boolean hasNext() {
                    return this._idx < this._count;
                }

                public Object next() {
                    return helper.get(value, this._idx++);
                }

                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        } else {
            return null;
        }
    }

    public Mustache.VariableFetcher createFetcher(Object ctx, String name) {
        if (ctx instanceof Mustache.CustomContext) {
            return CUSTOM_FETCHER;
        } else if (ctx instanceof Map) {
            return MAP_FETCHER;
        } else {
            char c = name.charAt(0);
            if (c >= '0' && c <= '9') {
                if (ctx instanceof List) {
                    return LIST_FETCHER;
                }

                if (ctx instanceof Iterator) {
                    return ITER_FETCHER;
                }

                if (ctx.getClass().isArray()) {
                    return arrayHelper(ctx);
                }
            }

            return null;
        }
    }

    public abstract <K, V> Map<K, V> createFetcherCache();

    protected static ArrayHelper arrayHelper(Object ctx) {
        if (ctx instanceof Object[]) {
            return OBJECT_ARRAY_HELPER;
        } else if (ctx instanceof boolean[]) {
            return BOOLEAN_ARRAY_HELPER;
        } else if (ctx instanceof byte[]) {
            return BYTE_ARRAY_HELPER;
        } else if (ctx instanceof char[]) {
            return CHAR_ARRAY_HELPER;
        } else if (ctx instanceof short[]) {
            return SHORT_ARRAY_HELPER;
        } else if (ctx instanceof int[]) {
            return INT_ARRAY_HELPER;
        } else if (ctx instanceof long[]) {
            return LONG_ARRAY_HELPER;
        } else if (ctx instanceof float[]) {
            return FLOAT_ARRAY_HELPER;
        } else {
            return ctx instanceof double[] ? DOUBLE_ARRAY_HELPER : null;
        }
    }

    protected abstract static class ArrayHelper implements Mustache.VariableFetcher {
        protected ArrayHelper() {
        }

        public Object get(Object ctx, String name) throws Exception {
            try {
                return this.get(ctx, Integer.parseInt(name));
            } catch (NumberFormatException var4) {
                return Template.NO_FETCHER_FOUND;
            } catch (ArrayIndexOutOfBoundsException var5) {
                return Template.NO_FETCHER_FOUND;
            }
        }

        public abstract int length(Object var1);

        protected abstract Object get(Object var1, int var2);
    }
}
