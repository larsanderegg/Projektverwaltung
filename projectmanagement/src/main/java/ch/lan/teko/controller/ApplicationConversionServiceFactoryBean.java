package ch.lan.teko.controller;

import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.roo.addon.web.mvc.controller.converter.RooConversionService;

import ch.lan.teko.model.Activity;
import ch.lan.teko.model.DocumentReference;
import ch.lan.teko.model.Employee;
import ch.lan.teko.model.FinanceResource;
import ch.lan.teko.model.Milestone;
import ch.lan.teko.model.PersonalResource;
import ch.lan.teko.model.Phase;
import ch.lan.teko.model.ProcessModel;
import ch.lan.teko.model.Project;

/**
 * A central place to register application converters and formatters.
 */
@RooConversionService
public class ApplicationConversionServiceFactoryBean extends FormattingConversionServiceFactoryBean {

	/** 
	 * {@inheritDoc}
	 */
	@Override
	protected void installFormatters(FormatterRegistry registry) {
		super.installFormatters(registry);
		// Register application converters and formatters
		installLabelConverters(registry);
	}

	private void installLabelConverters(FormatterRegistry registry) {
		registry.addConverter(getActivityToStringConverter());
		registry.addConverter(getIdToActivityConverter());
		registry.addConverter(getStringToActivityConverter());
		registry.addConverter(getDocumentReferenceToStringConverter());
		registry.addConverter(getIdToDocumentReferenceConverter());
		registry.addConverter(getStringToDocumentReferenceConverter());
		registry.addConverter(getEmployeeToStringConverter());
		registry.addConverter(getIdToEmployeeConverter());
		registry.addConverter(getStringToEmployeeConverter());
		registry.addConverter(getFinanceResourceToStringConverter());
		registry.addConverter(getIdToFinanceResourceConverter());
		registry.addConverter(getStringToFinanceResourceConverter());
		registry.addConverter(getMilestoneToStringConverter());
		registry.addConverter(getIdToMilestoneConverter());
		registry.addConverter(getStringToMilestoneConverter());
		registry.addConverter(getPersonalResourceToStringConverter());
		registry.addConverter(getIdToPersonalResourceConverter());
		registry.addConverter(getStringToPersonalResourceConverter());
		registry.addConverter(getPhaseToStringConverter());
		registry.addConverter(getIdToPhaseConverter());
		registry.addConverter(getStringToPhaseConverter());
		registry.addConverter(getProcessModelToStringConverter());
		registry.addConverter(getIdToProcessModelConverter());
		registry.addConverter(getStringToProcessModelConverter());
		registry.addConverter(getProjectToStringConverter());
		registry.addConverter(getIdToProjectConverter());
		registry.addConverter(getStringToProjectConverter());
	}

	private Converter<Activity, String> getActivityToStringConverter() {
		return new Converter<Activity, String>() {
			public String convert(Activity activity) {
				return new StringBuilder().append(activity.getName()).append(' ').append(activity.getStartDate())
						.append(' ').append(activity.getEndDate()).append(' ').append(activity.getPlanedStartDate())
						.toString();
			}
		};
	}

	private Converter<Long, Activity> getIdToActivityConverter() {
		return new Converter<Long, Activity>() {
			public Activity convert(Long id) {
				return Activity.findActivity(id);
			}
		};
	}

	private Converter<String, Activity> getStringToActivityConverter() {
		return new Converter<String, Activity>() {
			public Activity convert(String id) {
				return getObject().convert(getObject().convert(id, Long.class), Activity.class);
			}
		};
	}

	private Converter<DocumentReference, String> getDocumentReferenceToStringConverter() {
		return new Converter<DocumentReference, String>() {
			public String convert(DocumentReference documentReference) {
				return new StringBuilder().append(documentReference.getName()).append(' ')
						.append(documentReference.getPath()).toString();
			}
		};
	}

	private Converter<Long, DocumentReference> getIdToDocumentReferenceConverter() {
		return new Converter<Long, DocumentReference>() {
			public DocumentReference convert(Long id) {
				return DocumentReference.findDocumentReference(id);
			}
		};
	}

	private Converter<String, DocumentReference> getStringToDocumentReferenceConverter() {
		return new Converter<String, DocumentReference>() {
			public DocumentReference convert(String id) {
				return getObject().convert(getObject().convert(id, Long.class), DocumentReference.class);
			}
		};
	}

	private Converter<Employee, String> getEmployeeToStringConverter() {
		return new Converter<Employee, String>() {
			public String convert(Employee employee) {
				return new StringBuilder().append(employee.getSurname()).append(' ').append(employee.getName())
						.append(' ').append(employee.getJob()).toString();
			}
		};
	}

	private Converter<Long, Employee> getIdToEmployeeConverter() {
		return new Converter<Long, Employee>() {
			public Employee convert(Long id) {
				return Employee.findEmployee(id);
			}
		};
	}

	private Converter<String, Employee> getStringToEmployeeConverter() {
		return new Converter<String, Employee>() {
			public Employee convert(String id) {
				return getObject().convert(getObject().convert(id, Long.class), Employee.class);
			}
		};
	}

	private Converter<FinanceResource, String> getFinanceResourceToStringConverter() {
		return new Converter<FinanceResource, String>() {
			public String convert(FinanceResource financeResource) {
				return new StringBuilder().append(financeResource.getPlaned()).append(' ')
						.append(financeResource.getEffectiv()).append(' ').append(financeResource.getExplanation())
						.append(' ').append(financeResource.getType()).toString();
			}
		};
	}

	private Converter<Long, FinanceResource> getIdToFinanceResourceConverter() {
		return new Converter<Long, FinanceResource>() {
			public FinanceResource convert(Long id) {
				return FinanceResource.findFinanceResource(id);
			}
		};
	}

	private Converter<String, FinanceResource> getStringToFinanceResourceConverter() {
		return new Converter<String, FinanceResource>() {
			public FinanceResource convert(String id) {
				return getObject().convert(getObject().convert(id, Long.class), FinanceResource.class);
			}
		};
	}

	private Converter<Milestone, String> getMilestoneToStringConverter() {
		return new Converter<Milestone, String>() {
			public String convert(Milestone milestone) {
				return new StringBuilder().append(milestone.getName()).append(' ').append(milestone.getPlanedDate())
						.toString();
			}
		};
	}

	private Converter<Long, Milestone> getIdToMilestoneConverter() {
		return new Converter<Long, Milestone>() {
			public Milestone convert(Long id) {
				return Milestone.findMilestone(id);
			}
		};
	}

	private Converter<String, Milestone> getStringToMilestoneConverter() {
		return new Converter<String, Milestone>() {
			public Milestone convert(String id) {
				return getObject().convert(getObject().convert(id, Long.class), Milestone.class);
			}
		};
	}

	private Converter<PersonalResource, String> getPersonalResourceToStringConverter() {
		return new Converter<PersonalResource, String>() {
			public String convert(PersonalResource personalResource) {
				return new StringBuilder().append(personalResource.getPlaned()).append(' ')
						.append(personalResource.getEffectiv()).append(' ').append(personalResource.getExplanation())
						.append(' ').append(personalResource.getJob()).toString();
			}
		};
	}

	private Converter<Long, PersonalResource> getIdToPersonalResourceConverter() {
		return new Converter<Long, PersonalResource>() {
			public PersonalResource convert(Long id) {
				return PersonalResource.findPersonalResource(id);
			}
		};
	}

	private Converter<String, PersonalResource> getStringToPersonalResourceConverter() {
		return new Converter<String, PersonalResource>() {
			public PersonalResource convert(String id) {
				return getObject().convert(getObject().convert(id, Long.class), PersonalResource.class);
			}
		};
	}

	private Converter<Phase, String> getPhaseToStringConverter() {
		return new Converter<Phase, String>() {
			public String convert(Phase phase) {
				return new StringBuilder().append(phase.getName()).append(' ').append(phase.getReviewDate()).append(' ')
						.append(phase.getApprovalDate()).append(' ').append(phase.getPlanedReviewDate()).toString();
			}
		};
	}

	private Converter<Long, Phase> getIdToPhaseConverter() {
		return new Converter<Long, Phase>() {
			public Phase convert(Long id) {
				return Phase.findPhase(id);
			}
		};
	}

	private Converter<String, Phase> getStringToPhaseConverter() {
		return new Converter<String, Phase>() {
			public Phase convert(String id) {
				return getObject().convert(getObject().convert(id, Long.class), Phase.class);
			}
		};
	}

	private Converter<ProcessModel, String> getProcessModelToStringConverter() {
		return new Converter<ProcessModel, String>() {
			public String convert(ProcessModel processModel) {
				return new StringBuilder().append(processModel.getName()).toString();
			}
		};
	}

	private Converter<Long, ProcessModel> getIdToProcessModelConverter() {
		return new Converter<Long, ProcessModel>() {
			public ProcessModel convert(Long id) {
				return ProcessModel.findProcessModel(id);
			}
		};
	}

	private Converter<String, ProcessModel> getStringToProcessModelConverter() {
		return new Converter<String, ProcessModel>() {
			public ProcessModel convert(String id) {
				return getObject().convert(getObject().convert(id, Long.class), ProcessModel.class);
			}
		};
	}

	private Converter<Project, String> getProjectToStringConverter() {
		return new Converter<Project, String>() {
			public String convert(Project project) {
				return new StringBuilder().append(project.getName()).append(' ').append(project.getProgress())
						.append(' ').append(project.getApprovalDate()).append(' ').append(project.getDescription())
						.toString();
			}
		};
	}

	private Converter<Long, Project> getIdToProjectConverter() {
		return new Converter<Long, Project>() {
			public Project convert(Long id) {
				return Project.findProject(id);
			}
		};
	}

	private Converter<String, Project> getStringToProjectConverter() {
		return new Converter<String, Project>() {
			public Project convert(String id) {
				return getObject().convert(getObject().convert(id, Long.class), Project.class);
			}
		};
	}
}
