package activitiTest;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.Test;

public class Test3 {

    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    // 部署流程定义 类路径从classpath
    @Test
    public void deoploymentProcessDefinition_classpath() {
        Deployment deployment = processEngine.getRepositoryService() // 与流程定义和部署对象相关的service
                .createDeployment()// 创建一个部署对象
                .name("流程定义")// 添加部署的名称
                .addClasspathResource("MyProcess1.bpmn")// 从classpath的资源中加载，一次只能加载一个文件
                // .addClasspathResource("diagrams/helloworld.png")// 从classpath的资源中加载，一次只能加载一个文件
                .deploy();// 完成部署
        System.out.println("部署ID：" + deployment.getId());
        System.out.println("部署名称:" + deployment.getName());

    }

    // 部署流程定义 zip
    @Test
    public void deploymentProcessDefinition_zip() {
        InputStream in = this.getClass().getClassLoader()
                .getResourceAsStream("diagrams/diagrams.zip");
        ZipInputStream zipInputStream = new ZipInputStream(in);
        Deployment deployment = processEngine.getRepositoryService()// 与流程定义和部署对象相关的service
                .createDeployment()// 创建一个部署对象
                .name("流程定义")// 添加部署
                .addZipInputStream(zipInputStream)// 指定zip格式的文件完成部署
                .deploy();
        System.out.println("部署ID：" + deployment.getId());
        System.out.println("部署名称:" + deployment.getName());
    }

    @Test
    public void findProcessDefinition() {
        List<ProcessDefinition> list = processEngine.getRepositoryService()//与流程定义和部署对象先相关的service
                .createProcessDefinitionQuery()//创建一个流程定义的查询
                /** 指定查询条件，where条件 */
                // .deploymentId(deploymentId) //使用部署对象ID查询
                // .processDefinitionId(processDefinitionId)//使用流程定义ID查询
                // .processDefinitionNameLike(processDefinitionNameLike)//使用流程定义的名称模糊查询
                .orderByProcessDefinitionVersion().asc()

            /* 排序 */

                // .orderByProcessDefinitionVersion().desc()

            /* 返回的结果集 */
                .list();// 返回一个集合列表，封装流程定义
        // .singleResult();//返回惟一结果集
        // .count();//返回结果集数量
        // .listPage(firstResult, maxResults);//分页查询
        if (list != null && list.size() > 0) {
            for (ProcessDefinition pd : list) {
                System.out.println("流程定义ID:" + pd.getId());// 流程定义的key+版本+随机生成数
                System.out.println("流程定义的名称:" + pd.getName());// 对应helloworld.bpmn文件中的name属性值
                System.out.println("流程定义的key:" + pd.getKey());// 对应helloworld.bpmn文件中的id属性值
                System.out.println("流程定义的版本:" + pd.getVersion());// 当流程定义的key值相同的相同下，版本升级，默认1
                System.out.println("资源名称bpmn文件:" + pd.getResourceName());
                System.out.println("资源名称png文件:" + pd.getDiagramResourceName());
                System.out.println("部署对象ID：" + pd.getDeploymentId());
                System.out.println("#########################################################");
            }
        }
    }

    // 附加功能，查询最新版本的流程定义
    @Test
    public void findLastVersionProcessDefinition() {
        List<ProcessDefinition> list = processEngine.getRepositoryService()
                .createProcessDefinitionQuery()
                .orderByProcessDefinitionVersion().asc()//使用流程定义的新版本排序
                .list();
        /**
         * Map<String,ProcessDefinition> map集合的key：流程定义的key map集合的value：流程定义的对象
         * map集合的特点：当map集合key值相同的情况下，后一次的值将替换前一次的值
         */
        Map<String, ProcessDefinition> map = new LinkedHashMap<String, ProcessDefinition>();
        if (list != null && list.size() > 0) {
            for (ProcessDefinition pd : list) {
                map.put(pd.getKey(), pd);
            }
        }
        List<ProcessDefinition> pdList = new ArrayList<ProcessDefinition>(map.values());
        if (pdList != null && pdList.size() > 0) {
            for (ProcessDefinition pd : pdList) {
                System.out.println("流程定义ID:" + pd.getId());// 流程定义的key+版本+随机生成数
                System.out.println("流程定义的名称:" + pd.getName());// 对应helloworld.bpmn文件中的name属性值
                System.out.println("流程定义的key:" + pd.getKey());// 对应helloworld.bpmn文件中的id属性值
                System.out.println("流程定义的版本:" + pd.getVersion());// 当流程定义的key值相同的相同下，版本升级，默认1
                System.out.println("资源名称bpmn文件:" + pd.getResourceName());
                System.out.println("资源名称png文件:" + pd.getDiagramResourceName());
                System.out.println("部署对象ID：" + pd.getDeploymentId());
                System.out
                        .println("#########################################################");
            }
        }
    }

    //查看流程图
    @Test
    public void viewPic() {
        String deploymentId = "10001";
        //获取图片资源名称
        List<String> list = processEngine.getRepositoryService()
                .getDeploymentResourceNames(deploymentId);

        // 定义图片资源名称 resourceName为act_ge_bytearray表中NAME_列的值
        String resourceName = "MyProcess1.bpmn";
        if (list != null && list.size() > 0) {
            for (String name : list) {
                if (name.indexOf(".png") >= 0) {
                    resourceName = name;
                }
            }
        }

        // 获取图片的输入流
        InputStream in = processEngine.getRepositoryService()
                .getResourceAsStream(deploymentId, resourceName);

        File file = new File("D:/" + resourceName);
        // 将输入流的图片写到D盘下
        Test3.copyFile(in, "D:/" + resourceName, true);
    }

    /**
     * 复制单个文件
     * <p>
     * 待复制的文件名
     * 目标文件名
     *
     * @param overlay 如果目标文件存在，是否覆盖
     * @return 如果复制成功返回true，否则返回false
     */
    public static boolean copyFile(InputStream in, String destFileName,
                                   boolean overlay) {
      /*  File srcFile = new File(srcFileName);

        // 判断源文件是否存在
        if (!srcFile.exists()) {
            System.out.println("源文件不存在");
            return false;
        } else if (!srcFile.isFile()) {
            System.out.println("不是文件");
            return false;
        }*/

        // 判断目标文件是否存在
        File destFile = new File(destFileName);
        if (destFile.exists()) {
            // 如果目标文件存在并允许覆盖
            if (overlay) {
                // 删除已经存在的目标文件，无论目标文件是目录还是单个文件
                new File(destFileName).delete();
            }
        } else {
            // 如果目标文件所在目录不存在，则创建目录
            if (!destFile.getParentFile().exists()) {
                // 目标文件所在目录不存在
                if (!destFile.getParentFile().mkdirs()) {
                    // 复制文件失败：创建目标文件所在目录失败
                    return false;
                }
            }
        }

        // 复制文件
        int byteread = 0; // 读取的字节数
        OutputStream out = null;

        try {
            out = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];

            while ((byteread = in.read(buffer)) != -1) {
                out.write(buffer, 0, byteread);
            }
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (out != null)
                    out.close();
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //删除流程定义(删除key相同的所有不同版本的流程定义)
    @Test
    public void delteProcessDefinitionByKey() {
        //流程定义的key
        String processDefinitionKey = "HelloWorld";
        //先使用流程定义的key查询流程定义，查询出所有版本
        List<ProcessDefinition> list = processEngine.getRepositoryService()
                .createProcessDefinitionQuery()
                .processDefinitionKey(processDefinitionKey)
                .list();
        //遍历
        if (list != null && list.size() > 0) {
            for (ProcessDefinition pd : list) {
                // 获取部署ID
                String deploymentId = pd.getDeploymentId();
                //      /*
                //       * 不带级联的删除， 只能删除没有启动的流程，如果流程启动，就会抛出异常
                //       */
                //       processEngine.getRepositoryService().deleteDeployment(deploymentId);

                /**
                 * 级联删除 不管流程是否启动，都可以删除
                 */
                processEngine.getRepositoryService().deleteDeployment(
                        deploymentId, true);

            }
        }


    }
}
