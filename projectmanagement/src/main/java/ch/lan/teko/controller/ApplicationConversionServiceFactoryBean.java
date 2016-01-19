package ch.lan.teko.controller;

import ch.lan.teko.model.Activity;
import ch.lan.teko.model.DocumentReference;
import ch.lan.teko.model.Employee;
import ch.lan.teko.model.FinanceResource;
import ch.lan.teko.model.Milestone;
import ch.lan.teko.model.PersonalResource;
import ch.lan.teko.model.Phase;
import ch.lan.teko.model.ProcessModel;
import ch.lan.teko.model.Project;
import ch.lan.teko.service.ActivityService;
import ch.lan.teko.service.DocumentReferenceService;
import ch.lan.teko.service.EmployeeService;
import ch.lan.teko.service.FinanceResourceService;
import ch.lan.teko.service.MilestoneService;
import ch.lan.teko.service.PersonalResourceService;
import ch.lan.teko.service.PhaseService;
import ch.lan.teko.service.ProcessModelService;
import ch.lan.teko.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.roo.addon.web.mvc.controller.converter.RooConversionService;

@Configurable
/**
 * A central place to register application converters and formatters. 
 */
@RooConversionService
public class ApplicationConversionServiceFactoryBean extends FormattingConversionServiceFactoryBean {

	@Override
	protected void installFormatters(FormatterRegistry registry) {
		super.installFormatters(registry);
		// Register application converters and formatters
	}

	@Autowired
    ActivityService activityService;

	@Autowired
    DocumentReferenceService documentReferenceService;

	@Autowired
    EmployeeService employeeService;

	@Autowired
    FinanceResourceService financeResourceService;

	@Autowired
    MilestoneService milestoneService;

	@Autowired
    PersonalResourceService personalResourceService;

	@Autowired
    PhaseService phaseService;

	@Autowired
    ProcessModelService processModelService;

	@Autowired
    ProjectService projectService;

	public Converter<Activity, String> getActivityToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<ch.lan.teko.model.Activity, java.lang.String>() {
            public String convert(Activity activity) {
                return new StringBuilder().append(activity.getStartDate()).append(' ').append(activity.getEndDate()).append(' ').append(activity.getPlanedStartDate()).append(' ').append(activity.getPlanedEndDate()).toString();
            }
        };
    }

	public Converter<Long, Activity> getIdToActivityConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, ch.lan.teko.model.Activity>() {
            public ch.lan.teko.model.Activity convert(java.lang.Long id) {
                return activityService.findActivity(id);
            }
        };
    }

	public Converter<String, Activity> getStringToActivityConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, ch.lan.teko.model.Activity>() {
            public ch.lan.teko.model.Activity convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Activity.class);
            }
        };
    }

	public Converter<DocumentReference, String> getDocumentReferenceToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<ch.lan.teko.model.DocumentReference, java.lang.String>() {
            public String convert(DocumentReference documentReference) {
                return new StringBuilder().append(documentReference.getName()).append(' ').append(documentReference.getPath()).toString();
            }
        };
    }

	public Converter<Long, DocumentReference> getIdToDocumentReferenceConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, ch.lan.teko.model.DocumentReference>() {
            public ch.lan.teko.model.DocumentReference convert(java.lang.Long id) {
                return documentReferenceService.findDocumentReference(id);
            }
        };
    }

	public Converter<String, DocumentReference> getStringToDocumentReferenceConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, ch.lan.teko.model.DocumentReference>() {
            public ch.lan.teko.model.DocumentReference convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), DocumentReference.class);
            }
        };
    }

	public Converter<Employee, String> getEmployeeToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<ch.lan.teko.model.Employee, java.lang.String>() {
            public String convert(Employee employee) {
                return new StringBuilder().append(employee.getName()).append(' ').append(employee.getSurname()).append(' ').append(employee.getPensum()).append(' ').append(employee.getJob()).toString();
            }
        };
    }

	public Converter<Long, Employee> getIdToEmployeeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, ch.lan.teko.model.Employee>() {
            public ch.lan.teko.model.Employee convert(java.lang.Long id) {
                return employeeService.findEmployee(id);
            }
        };
    }

	public Converter<String, Employee> getStringToEmployeeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, ch.lan.teko.model.Employee>() {
            public ch.lan.teko.model.Employee convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Employee.class);
            }
        };
    }

	public Converter<FinanceResource, String> getFinanceResourceToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<ch.lan.teko.model.FinanceResource, java.lang.String>() {
            public String convert(FinanceResource financeResource) {
                return new StringBuilder().append(financeResource.getPlaned()).append(' ').append(financeResource.getEffectiv()).append(' ').append(financeResource.getExplanation()).append(' ').append(financeResource.getType()).toString();
            }
        };
    }

	public Converter<Long, FinanceResource> getIdToFinanceResourceConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, ch.lan.teko.model.FinanceResource>() {
            public ch.lan.teko.model.FinanceResource convert(java.lang.Long id) {
                return financeResourceService.findFinanceResource(id);
            }
        };
    }

	public Converter<String, FinanceResource> getStringToFinanceResourceConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, ch.lan.teko.model.FinanceResource>() {
            public ch.lan.teko.model.FinanceResource convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), FinanceResource.class);
            }
        };
    }

	public Converter<Milestone, String> getMilestoneToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<ch.lan.teko.model.Milestone, java.lang.String>() {
            public String convert(Milestone milestone) {
                return new StringBuilder().append(milestone.getName()).append(' ').append(milestone.getPlanedDate()).toString();
            }
        };
    }

	public Converter<Long, Milestone> getIdToMilestoneConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, ch.lan.teko.model.Milestone>() {
            public ch.lan.teko.model.Milestone convert(java.lang.Long id) {
                return milestoneService.findMilestone(id);
            }
        };
    }

	public Converter<String, Milestone> getStringToMilestoneConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, ch.lan.teko.model.Milestone>() {
            public ch.lan.teko.model.Milestone convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Milestone.class);
            }
        };
    }

	public Converter<PersonalResource, String> getPersonalResourceToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<ch.lan.teko.model.PersonalResource, java.lang.String>() {
            public String convert(PersonalResource personalResource) {
                return new StringBuilder().append(personalResource.getPlaned()).append(' ').append(personalResource.getEffectiv()).append(' ').append(personalResource.getExplanation()).append(' ').append(personalResource.getJob()).toString();
            }
        };
    }

	public Converter<Long, PersonalResource> getIdToPersonalResourceConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, ch.lan.teko.model.PersonalResource>() {
            public ch.lan.teko.model.PersonalResource convert(java.lang.Long id) {
                return personalResourceService.findPersonalResource(id);
            }
        };
    }

	public Converter<String, PersonalResource> getStringToPersonalResourceConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, ch.lan.teko.model.PersonalResource>() {
            public ch.lan.teko.model.PersonalResource convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), PersonalResource.class);
            }
        };
    }

	public Converter<Phase, String> getPhaseToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<ch.lan.teko.model.Phase, java.lang.String>() {
            public String convert(Phase phase) {
                return new StringBuilder().append(phase.getReviewDate()).append(' ').append(phase.getApprovalDate()).append(' ').append(phase.getPlanedReviewDate()).append(' ').append(phase.getProgress()).toString();
            }
        };
    }

	public Converter<Long, Phase> getIdToPhaseConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, ch.lan.teko.model.Phase>() {
            public ch.lan.teko.model.Phase convert(java.lang.Long id) {
                return phaseService.findPhase(id);
            }
        };
    }

	public Converter<String, Phase> getStringToPhaseConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, ch.lan.teko.model.Phase>() {
            public ch.lan.teko.model.Phase convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Phase.class);
            }
        };
    }

	public Converter<ProcessModel, String> getProcessModelToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<ch.lan.teko.model.ProcessModel, java.lang.String>() {
            public String convert(ProcessModel processModel) {
                return new StringBuilder().append(processModel.getName()).append(' ').append(processModel.getPhases()).toString();
            }
        };
    }

	public Converter<Long, ProcessModel> getIdToProcessModelConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, ch.lan.teko.model.ProcessModel>() {
            public ch.lan.teko.model.ProcessModel convert(java.lang.Long id) {
                return processModelService.findProcessModel(id);
            }
        };
    }

	public Converter<String, ProcessModel> getStringToProcessModelConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, ch.lan.teko.model.ProcessModel>() {
            public ch.lan.teko.model.ProcessModel convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ProcessModel.class);
            }
        };
    }

	public Converter<Project, String> getProjectToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<ch.lan.teko.model.Project, java.lang.String>() {
            public String convert(Project project) {
                return new StringBuilder().append(project.getProgress()).append(' ').append(project.getApprovalDate()).append(' ').append(project.getName()).append(' ').append(project.getDescription()).toString();
            }
        };
    }

	public Converter<Long, Project> getIdToProjectConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, ch.lan.teko.model.Project>() {
            public ch.lan.teko.model.Project convert(java.lang.Long id) {
                return projectService.findProject(id);
            }
        };
    }

	public Converter<String, Project> getStringToProjectConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, ch.lan.teko.model.Project>() {
            public ch.lan.teko.model.Project convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Project.class);
            }
        };
    }

	public void installLabelConverters(FormatterRegistry registry) {
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

	public void afterPropertiesSet() {
        super.afterPropertiesSet();
        installLabelConverters(getObject());
    }
}
