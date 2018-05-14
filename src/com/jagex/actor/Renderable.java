package com.jagex.actor;

import com.jagex.actor.model.Model;
import com.jagex.actor.model.VertexNormal;
import com.jagex.collection.Cacheable;

public class Renderable extends Cacheable {

    public int modelBaseY;
    public VertexNormal vertexNormals[];

    public void renderAtPoint(int i, int j, int k, int l, int i1, int j1, int k1, int l1, int i2) {
        Model model = getRotatedModel();
        if (model != null) {
            modelBaseY = model.modelBaseY;
            model.renderAtPoint(i, j, k, l, i1, j1, k1, l1, i2);
        }
    }

    public Model getRotatedModel() {
        return null;
    }

    public Renderable() {
        modelBaseY = 1000;
    }
}