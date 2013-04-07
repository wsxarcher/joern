package main.codeitems.functionDef;

import main.codeitems.CodeItem;
import main.codeitems.Identifier;

import org.antlr.v4.runtime.ParserRuleContext;

import antlr.CodeSensorParser.Parameter_declContext;
import antlr.CodeSensorParser.Parameter_idContext;
import antlr.CodeSensorParser.Parameter_nameContext;

public class Parameter extends CodeItem
{
	public ParameterType type = new ParameterType();
	public Identifier name = new Identifier();
	
	public Parameter()
	{
		setNodeTypeName("PARAMETER");
	}
	
	@Override
	public void initializeFromContext(ParserRuleContext ctx)
	{
		Parameter_declContext paramCtx = (Parameter_declContext) ctx;
		Parameter_nameContext paramName = getNameOfParameter(paramCtx);
		
		type.initializeFromContext(ctx);
		name.initializeFromContext(paramName);
		super.initializeFromContext(ctx);
	}
	
	private Parameter_nameContext getNameOfParameter(Parameter_declContext param_ctx)
	{
		Parameter_idContext parameter_id = param_ctx.parameter_id();
		
		while(parameter_id.parameter_name() == null){
			parameter_id = parameter_id.parameter_id();
		}
		return parameter_id.parameter_name();
	}

	
}