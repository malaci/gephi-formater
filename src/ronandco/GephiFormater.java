package ronandco;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.EdgeDirectionDefault;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.layout.plugin.AutoLayout;
import org.gephi.layout.plugin.forceAtlas.ForceAtlas;
import org.gephi.layout.plugin.forceAtlas.ForceAtlasLayout;
import org.gephi.layout.plugin.fruchterman.FruchtermanReingold;
import org.gephi.layout.plugin.fruchterman.FruchtermanReingoldBuilder;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

/**
 * This demo focuses on Import and Export features, showing different IO
 * possibilities.
 * <p>
 * Import can be performed from a file, database or Reader/InputStream. The
 * export can be done to files and Writer/OutputStream. The demo import a file
 * and shows how to configure graph export to use the visible graph instead of
 * the full graph. That is essential to export a graph that has been filtered.
 * <p>
 * The ability to export graph or PDF to Writer or Byte array is showed at the
 * end.
 *
 * @author Mathieu Bastian
 */
public class GephiFormater {

    public void script(String in_file,String out_file,String algo,int procTime) {
        //Init a project - and therefore a workspace
        ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
        pc.newProject();
        Workspace workspace = pc.getCurrentWorkspace();

        //Get controllers and models
        ImportController importController = Lookup.getDefault().lookup(ImportController.class);

        //Import file
        Container container;
        try {
            File file = new File(in_file);
            container = importController.importFile(file);
            container.getLoader().setEdgeDefault(EdgeDirectionDefault.UNDIRECTED);   //Force UNDIRECTED
            container.getLoader().setAllowAutoNode(false);  //Don't create missing nodes
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

        //Append imported data to GraphAPI
        importController.process(container, new DefaultProcessor(), workspace);

         //Get a graph model - it exists because we have a workspace
        GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getGraphModel(workspace);
                
        AutoLayout autoLayout = new AutoLayout(procTime, TimeUnit.SECONDS);
        autoLayout.setGraphModel(graphModel);
        
        if(algo.equals("1")){
        	ForceAtlasLayout secondLayout = new ForceAtlasLayout(null);
        	autoLayout.addLayout(secondLayout, 1.0f);
        	autoLayout.execute();
        }
        
        if(algo.equals("2")){
        	
	        FruchtermanReingold firstLayout = new FruchtermanReingold( new FruchtermanReingoldBuilder());
//	        firstLayout.resetPropertiesValues();
	        AutoLayout.DynamicProperty setGravity = AutoLayout.createDynamicProperty("fruchtermanReingold.gravity.name",new Double(5.), 0f);//True after 10% of layout time
//	        AutoLayout.DynamicProperty setArea = AutoLayout.createDynamicProperty("forceAtlas.repulsionStrength.name", new Double(500.), 0f);//500 for the complete period	        
//	        firstLayout.setArea(50000.0f);
//	        firstLayout.setGravity(5.0);
//	        firstLayout.setSpeed(1.0);
	        autoLayout.addLayout(firstLayout, 1.0f,new AutoLayout.DynamicProperty[]{setGravity});
	        autoLayout.execute();
        	
        }
        
        //Export full graph
        ExportController ec = Lookup.getDefault().lookup(ExportController.class);
        try {
            ec.exportFile(new File(out_file));
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }

    }
}