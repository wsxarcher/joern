package databaseNodes;

import java.util.HashMap;
import java.util.Map;

import ast.ASTNode;
import ast.CodeLocation;
import ast.functionDef.FunctionDef;
import cdg.CDG;
import cdg.CDGCreator;
import cfg.ASTToCFGConverter;
import cfg.CFG;
import cfg.CFGFactory;
import cfg.nodes.CFGNode;
import ddg.CFGAndUDGToDefUseCFG;
import ddg.DDGCreator;
import ddg.DataDependenceGraph.DDG;
import ddg.DefUseCFG.DefUseCFG;
import dom.DominatorTree;
import udg.CFGToUDGConverter;
import udg.useDefAnalysis.ASTDefUseAnalyzer;
import udg.useDefGraph.UseDefGraph;

// Note: we currently use the FunctionDatabaseNode
// as a container for the Function. That's not very
// clean. We should have a sep. Function-Class.

public class FunctionDatabaseNode extends DatabaseNode
{
	FunctionDef astRoot;
	CFG cfg;
	UseDefGraph udg;
	DDG ddg;
	CDG cdg;
	DominatorTree<CFGNode> dom;
	DominatorTree<CFGNode> postDom;

	String signature;
	String name;

	ASTToCFGConverter astToCFG = new ASTToCFGConverter();
	CFGToUDGConverter cfgToUDG = new CFGToUDGConverter();
	CFGAndUDGToDefUseCFG udgAndCfgToDefUseCFG = new CFGAndUDGToDefUseCFG();
	DDGCreator ddgCreator = new DDGCreator();
	CDGCreator cdgCreator = new CDGCreator();

	public void setCFGFactory(CFGFactory factory)
	{
		astToCFG.setFactory(factory);
	}

	public void setASTDefUseAnalyzer(ASTDefUseAnalyzer analyzer)
	{
		cfgToUDG.setASTDefUseAnalyzer(analyzer);
	}

	@Override
	public void initialize(Object node)
	{
		astRoot = (FunctionDef) node;
		cfg = astToCFG.convert(astRoot);
		dom = DominatorTree.newDominatorTree(cfg);
		postDom = DominatorTree.newPostDominatorTree(cfg);
		udg = cfgToUDG.convert(cfg);
		DefUseCFG defUseCFG = udgAndCfgToDefUseCFG.convert(cfg, udg);
		ddg = ddgCreator.createForDefUseCFG(defUseCFG);
		cdg = CDGCreator.create(cfg, postDom);

		setSignature(astRoot);
	}

	@Override
	public Map<String, Object> createProperties()
	{
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(NodeKeys.TYPE, "Function");
		properties.put(NodeKeys.LOCATION, this.getLocation());
		properties.put(NodeKeys.NAME, this.getName());
		// properties.put("signature", this.getSignature());
		return properties;
	}

	public String getName()
	{
		return astRoot.getIdentifier().getEscapedCodeStr();
	}

	public ASTNode getASTRoot()
	{
		return astRoot;
	}

	public CFG getCFG()
	{
		return cfg;
	}

	public UseDefGraph getUDG()
	{
		return udg;
	}

	public DDG getDDG()
	{
		return ddg;
	}

	public CDG getCDG()
	{
		return cdg;
	}

	public DominatorTree<CFGNode> getDominatorTree()
	{
		return dom;
	}

	public DominatorTree<CFGNode> getPostDominatorTree()
	{
		return postDom;
	}

	public String getLocation()
	{
		return astRoot.getLocationString();
	}

	public CodeLocation getContentLocation()
	{
		return astRoot.getContent().getLocation();
	}

	public String getSignature()
	{
		return signature;
	}

	private void setSignature(FunctionDef node)
	{
		signature = node.getFunctionSignature();
	}

}
