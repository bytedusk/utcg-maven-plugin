//import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
//import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.InstantiationStrategy;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.apache.maven.settings.Settings;

@Mojo(name = "goal1")
public class TestMojo extends AbstractMojo {

    @Parameter(name = "name", defaultValue = "test")
    private String name;

    @Parameter(property = "utcg.generator.outputDirectory",
            defaultValue = "${project.build.testSourceDirectory}", required = true)
    private String utcgOutputDirectory;

    public void execute() {
        getLog().info("This is goal1.");
        getLog().info("utcgOutputDirectory:"+utcgOutputDirectory);

    }
}

//import org.apache.maven.execution.MavenSession;
//        import org.apache.maven.plugin.AbstractMojo;
//        import org.apache.maven.plugin.MojoExecution;
//        import org.apache.maven.plugin.descriptor.PluginDescriptor;
//        import org.apache.maven.plugins.annotations.Component;
//        import org.apache.maven.plugins.annotations.Execute;
//        import org.apache.maven.plugins.annotations.InstantiationStrategy;
//        import org.apache.maven.plugins.annotations.LifecyclePhase;
//        import org.apache.maven.plugins.annotations.Mojo;
//        import org.apache.maven.plugins.annotations.Parameter;
//        import org.apache.maven.plugins.annotations.ResolutionScope;
//        import org.apache.maven.project.MavenProject;
//        import org.apache.maven.settings.Settings;
//
//// 此Mojo对应的目标的名称
//@Mojo( name = "<goal-name>",
//        aggregator = <false|true>,
//        configurator = "<role hint>",
//        // 执行策略
//        executionStrategy = "<once-per-session|always>",
//        inheritByDefault = <true|false>,
//        // 实例化策略
//        instantiationStrategy = InstantiationStrategy.<strategy>,
//        // 如果用户没有在POM中明确设置此Mojo绑定到的phase，那么绑定一个MojoExecution到那个phase
//        defaultPhase = LifecyclePhase.<phase>,
//        requiresDependencyResolution = ResolutionScope.<scope>,
//        requiresDependencyCollection = ResolutionScope.<scope>,
//        // 提示此Mojo需要被直接调用（而非绑定到生命周期阶段）
//        requiresDirectInvocation = <false|true>,
//        // 提示此Mojo不能在离线模式下运行
//        requiresOnline = <false|true>,
//        // 提示此Mojo必须在一个Maven项目内运行
//        requiresProject = <true|false>,
//        // 提示此Mojo是否线程安全，线程安全的Mojo支持在并行构建中被并发的调用
//        threadSafe = <false|true> ) // (since Maven 3.0)
//
//// 何时执行此Mojo
//@Execute( goal = "<goal-name>",           // 如果提供goal，则隔离执行此Mojo
//        phase = LifecyclePhase.<phase>, // 在此生命周期阶段自动执行此Mojo
//        lifecycle = "<lifecycle-id>" )  // 在此生命周期中执行此Mojo
//public class MyMojo
//        extends AbstractMojo
//{
//
//    @Parameter( name = "parameter",
//            // 在POM中可使用别名来配置参数
//            alias = "myAlias",
//            property = "a.property",
//            defaultValue = "an expression, possibly with ${variables}",
//            readonly = <false|true>,
//            required = <false|true> )
//    private String parameter;
//
//    @Component( role = MyComponentExtension.class,
//            hint = "..." )
//    private MyComponent component;
//
//
//    @Parameter( defaultValue = "${session}", readonly = true )
//    private MavenSession session;
//
//    @Parameter( defaultValue = "${project}", readonly = true )
//    private MavenProject project;
//
//    @Parameter( defaultValue = "${mojoExecution}", readonly = true )
//    private MojoExecution mojo;
//
//    @Parameter( defaultValue = "${plugin}", readonly = true )
//    private PluginDescriptor plugin;
//
//    @Parameter( defaultValue = "${settings}", readonly = true )
//    private Settings settings;
//
//    @Parameter( defaultValue = "${project.basedir}", readonly = true )
//    private File basedir;
//
//    @Parameter( defaultValue = "${project.build.directory}", readonly = true )
//    private File target;
//
//    public void execute()
//    {
//    }
