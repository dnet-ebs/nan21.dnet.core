package net.nan21.dnet.core.presenter.descriptor;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import net.nan21.dnet.core.api.annotation.JobParam;
import net.nan21.dnet.core.api.descriptor.IJobDefinition;

@XmlRootElement(name = "jobDefinition")
@XmlAccessorType(XmlAccessType.FIELD)
public class JobDefinition implements IJobDefinition {
	/**
	 * Business name of the job used to identify the component when scheduling
	 * its execution.
	 */
	private String name;

	/**
	 * Job java class.
	 */
	private Class<?> javaClass;

	@XmlElementWrapper(name = "jobParams")
	@XmlElement(name = "field")
	private List<FieldDefinition> jobParams;

	private void resolveFields(Class<?> claz, List<FieldDefinition> fieldsList) {
		if (claz != null) {
			Field[] fields = claz.getDeclaredFields();
			for (Field field : fields) {
				if (field.isAnnotationPresent(JobParam.class)
						&& !Modifier.isStatic(field.getModifiers())) {
					String fieldName = field.getName();
					fieldsList.add(new FieldDefinition(fieldName, field
							.getType().getCanonicalName()));
				}

			}
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Class<?> getJavaClass() {
		return javaClass;
	}

	public void setJavaClass(Class<?> javaClass) {
		this.javaClass = javaClass;
	}

	public List<FieldDefinition> getJobParams() {
		if (this.jobParams == null) {
			this.jobParams = new ArrayList<FieldDefinition>();
			this.resolveFields(this.javaClass, jobParams);
		}
		return jobParams;
	}

	public void setJobParams(List<FieldDefinition> jobParams) {
		this.jobParams = jobParams;
	}

}
