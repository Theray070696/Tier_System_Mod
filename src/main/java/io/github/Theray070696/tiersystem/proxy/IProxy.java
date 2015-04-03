package io.github.Theray070696.tiersystem.proxy;

import cpw.mods.fml.relauncher.Side;

/**
 * Created by Theray on 10/4/14.
 */
public interface IProxy
{

    public Side getSide();

    public void loadCapes();
}