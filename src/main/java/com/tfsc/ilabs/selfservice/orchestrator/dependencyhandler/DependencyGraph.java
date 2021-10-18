package com.tfsc.ilabs.selfservice.orchestrator.dependencyhandler;

import com.tfsc.ilabs.selfservice.common.exception.InvalidArgumentsException;
import com.tfsc.ilabs.selfservice.common.models.ErrorObject;

import java.util.*;

public class DependencyGraph<T> {

    private Set<T> allItems;

    // key depends on the value. ie. only after all items in the value are completed, the key item can be started.
    private Map<T, Set<T>> itemToDependenciesMap;

    public DependencyGraph(Map<T, Set<T>> itemToDependenciesMap, Set<T> all) {
        this.itemToDependenciesMap = itemToDependenciesMap;
        this.allItems = all;
        if (hasCycle(itemToDependenciesMap)) {
            throw new InvalidArgumentsException(new ErrorObject("cycle detected while constructing dependency graph: {0}", itemToDependenciesMap));
        }
    }

    static <T> boolean hasCycle(Map<T, Set<T>> itemToDependenciesMap) {
        for (T key : itemToDependenciesMap.keySet()) {
            if (hasCycle(itemToDependenciesMap, new ArrayList<>(), key)) {
                return true;
            }
        }
        return false;
    }

    private static <T> boolean hasCycle(Map<T, Set<T>> itemToDependenciesMap, List<T> ancestors, T current) {
        Set<T> dependencies = itemToDependenciesMap.get(current);
        ancestors = new ArrayList<>(ancestors);
        ancestors.add(current);
        if (dependencies != null) {
            for (T dependency : dependencies) {
                if (ancestors.contains(dependency)) {
                    return true;
                }
                if (hasCycle(itemToDependenciesMap, ancestors, dependency)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Map<T, Set<T>> getItemToDependenciesMap() {
        return itemToDependenciesMap;
    }

    public Set<T> start() {
        Set<T> temp = new HashSet<>(allItems);
        temp.removeAll(itemToDependenciesMap.keySet());
        return temp;
    }

    public Set<T> itemCompleted(T t) {
        allItems.remove(t);

        Set<T> itemsReady = new HashSet<>();
        for (Map.Entry<T, Set<T>> entry : itemToDependenciesMap.entrySet()) {
            entry.getValue().remove(t);
            if (entry.getValue().isEmpty()) {
                itemsReady.add(entry.getKey());
            }
        }
        for (T readyItem : itemsReady) {
            itemToDependenciesMap.remove(readyItem);
        }

        return itemsReady;
    }

    public boolean hasFinished() {
        return allItems.isEmpty();
    }

    @Override
    public String toString() {
        return "DependencyGraph{" +
                "allItems=" + allItems +
                ", itemToDependenciesMap=" + itemToDependenciesMap +
                '}';
    }
}
