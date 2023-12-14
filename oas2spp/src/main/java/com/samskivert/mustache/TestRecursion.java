package com.samskivert.mustache;


import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lidong@date 2023-11-18@version 1.0
 */
public class TestRecursion {
    static class Component{
        int id;
        List<Component> children = new ArrayList<>();

        public Component(int id) {
            this.id = id;
        }

        public Component add(Component child){
            children.add(child);
            return this;
        }

        @Override
        public String toString() {
            return "Component{" +
                    "id=" + id +
                    ", children=" + children +
                    '}';
        }
    }
    public static void main(String[] args) {
        String musta = "{{id}}\n{{#children}}\t{{id}}\n{{#children}}\t{{/children}}{{/children}}";
        Template template = Mustache.compiler().compile(new StringReader(musta));
        Component root = new Component(1);
        root.add(new Component(2).add(new Component(3).add(new Component(6))).add(new Component(5)));
        root.add(new Component(4));
        System.out.println(root);
        System.out.println(template.execute(root));
    }
}
