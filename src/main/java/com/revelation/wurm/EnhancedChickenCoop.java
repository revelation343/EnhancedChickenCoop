package com.revelation.wurm;

import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import javassist.ClassPool;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.gotti.wurmunlimited.modloader.interfaces.Configurable;
import org.gotti.wurmunlimited.modloader.interfaces.PreInitable;
import org.gotti.wurmunlimited.modloader.interfaces.WurmServerMod;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


public class EnhancedChickenCoop implements WurmServerMod, Configurable, PreInitable{

		private Integer eggQL = -1;
		private Logger logger = Logger.getLogger(this.getClass().getName());

		public void configure(Properties properties){
			eggQL = Integer.parseInt(properties.getProperty("EggQL", Integer.toString(eggQL)));
		}

		public void preInit() {
			try {
				final Class<EnhancedChickenCoop> thisClass = EnhancedChickenCoop.class;
				String replace;
				ClassPool classPool = HookManager.getInstance().getClassPool();
				classPool.getCtClass("com.wurmonline.server.items.ChickenCoops")
						.getMethod("eggPoller", "(Lcom/wurmonline/server/items/Item;)V")
						.instrument(new ExprEditor() {
							@Override
							public void edit(MethodCall m) throws CannotCompileException {
								if (m.getMethodName().equals("getQualityLevel"))
									m.replace("$_="+eggQL+"f;");
							}
						});
			}
			catch (CannotCompileException e) {
					e.printStackTrace();}
			catch (NotFoundException e) {
					e.printStackTrace();}
			logger.log(Level.INFO,"Enhanced Chicken Coop Initialized");
		}
}