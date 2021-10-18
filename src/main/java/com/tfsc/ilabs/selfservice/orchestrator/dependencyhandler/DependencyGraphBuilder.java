package com.tfsc.ilabs.selfservice.orchestrator.dependencyhandler;

import java.util.*;

public class DependencyGraphBuilder<T> {

    private Set<T> all = new HashSet<>();

    private Map<T, Set<T>> graph = new HashMap<>();

    public void add(T t) {
        all.add(t);
    }

    public void addDependency(T from, T to) {
        Set<T> dependencies = graph.computeIfAbsent(from, k -> new HashSet<>());
        dependencies.add(to);
        all.add(from);
        all.add(to);
    }

    public void addDependencies(T from, Collection<T> to) {
        for(T t: to) {
            addDependency(from, t);
        }
    }

    public boolean hasCycle() {
        return DependencyGraph.hasCycle(graph);
    }

    public DependencyGraph<T> build() {
        return new DependencyGraph<>(graph, all);
    }

    @Override
    public String toString() {
        return "DependencyGraphBuilder{" +
                "all=" + all +
                ", graph=" + graph +
                '}';
    }
}
