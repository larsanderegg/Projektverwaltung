package ch.lan.teko.model;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Test;

public class TimeBoxedDataTest {

	@Test
	public void addViaCtor() {
		TimeBoxedData tbd = new TimeBoxedData(LocalDate.now(), LocalDate.now(), LocalDate.now(), LocalDate.now());
		
		assertEquals(LocalDate.now(), tbd.getStartDate());
		assertEquals(LocalDate.now(), tbd.getEndDate());
		assertEquals(LocalDate.now(), tbd.getPlanedEndDate());
		assertEquals(LocalDate.now(), tbd.getPlanedStartDate());
	}
	
	@Test
	public void calculateDuration() {
		TimeBoxedData tbd1 = new TimeBoxedData(LocalDate.now().minusDays(1), LocalDate.now(), LocalDate.now().minusWeeks(1), LocalDate.now());
		TimeBoxedData tbd2 = new TimeBoxedData(LocalDate.now().minusDays(1), LocalDate.now().plusDays(1), LocalDate.now().minusWeeks(1), LocalDate.now().plusWeeks(1));
		TimeBoxedData tbd3 = new TimeBoxedData(LocalDate.now().plusDays(2), LocalDate.now().minusDays(1), LocalDate.now().plusWeeks(1), LocalDate.now().minusWeeks(1));
		
		ITimeBoxed timeBoxed = new ITimeBoxed() {
			
			private int i = 0;
			private TimeBoxedData[] data = new TimeBoxedData[]{tbd1, tbd2, tbd3};
			
			@Override
			public TimeBoxedData getTimeBoxedData() {
				TimeBoxedData result = data[i];
				i++;
				return result;
			}
		};
		
		TimeBoxedData tbd4 = new TimeBoxedData();
		tbd4.add(timeBoxed);
		tbd4.add(timeBoxed);
		tbd4.add(timeBoxed);
		
		assertEquals(LocalDate.now().minusDays(1), tbd4.getStartDate());
		assertEquals(LocalDate.now().plusDays(1), tbd4.getEndDate());
		assertEquals(LocalDate.now().plusWeeks(1), tbd4.getPlanedEndDate());
		assertEquals(LocalDate.now().minusWeeks(1), tbd4.getPlanedStartDate());
	}
}
