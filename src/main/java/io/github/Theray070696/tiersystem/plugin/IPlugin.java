/**
 * This class was created by <Theray070696>. It's distributed as
 * part of the Inheritance Mod.
 *
 * Inheritance is Open Source
 *
 * File Created @ [Dec 4, 2013, 10:10:10 AM (EST)]
 */
package io.github.Theray070696.tiersystem.plugin;

public interface IPlugin
{

    public String getModID();

    public void preInit();

    public void init();

    public void postInit();
}